package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.entrada.MotorEntrada;
import com.neoenergia.neodemanda.motor.norma.ParametroCalculo;
import com.neoenergia.neodemanda.motor.norma.Tabela19MotoresCvKva;
import com.neoenergia.neodemanda.motor.saida.PassoCalculo;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraParcelaG extends RegraDemanda {

	private static final String ORIGEM_DA_REGRA = "Regra de demanda de motores da Tabela 14; a norma dessa tabela "
			+ "não está identificada na documentação";

	RegraParcelaG() {
		super("G", "Parcela de motores elétricos", "g", ParametroCalculo.FATOR_MAIOR_MOTOR.referencia());
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		BigDecimal fatorMaior = ParametroCalculo.FATOR_MAIOR_MOTOR.valor();
		BigDecimal fatorDemais = ParametroCalculo.FATOR_DEMAIS_MOTORES.valor();
		String textoFatorMaior = FormatoMemorial.numero(fatorMaior);
		String textoFatorDemais = FormatoMemorial.numero(fatorDemais);
		String formula = "g = (Dm_max × " + textoFatorMaior + ") + ∑ (Dm_j × " + textoFatorDemais + ")";
		List<MotorEntrada> motores = contexto.entrada().motores();

		if (motores.isEmpty()) {
			contexto.registrar(ChaveResultado.G, BigDecimal.ZERO);
			PassoCalculo.Builder passo = passo(contexto, formula, "g = 0", BigDecimal.ZERO, UnidadeMedida.KVA)
					.condicao("Sem motores. " + ORIGEM_DA_REGRA)
					.entrada("Quantidade de motores", BigDecimal.ZERO, UnidadeMedida.ADIMENSIONAL);
			citar(passo, ParametroCalculo.FATOR_MAIOR_MOTOR, ParametroCalculo.FATOR_DEMAIS_MOTORES);
			contexto.memoria().adicionar(passo.build());
			return;
		}

		List<BigDecimal> demandas = new ArrayList<>();
		int indiceMaior = 0;
		for (int i = 0; i < motores.size(); i++) {
			demandas.add(Tabela19MotoresCvKva.porPotenciaCv(motores.get(i).potenciaCv()).demandaKva());
			if (demandas.get(i).compareTo(demandas.get(indiceMaior)) > 0) {
				indiceMaior = i;
			}
		}

		BigDecimal dmMax = demandas.get(indiceMaior);
		BigDecimal g = dmMax.multiply(fatorMaior);
		List<String> termosDemais = new ArrayList<>();
		for (int i = 0; i < motores.size(); i++) {
			int unidades = motores.get(i).quantidade() - (i == indiceMaior ? 1 : 0);
			if (unidades == 0) {
				continue;
			}
			BigDecimal demanda = demandas.get(i);
			g = g.add(demanda.multiply(quantidade(unidades)).multiply(fatorDemais));
			String textoUnidades = unidades == 1 ? "" : " × " + unidades;
			termosDemais.add("(" + FormatoMemorial.numero(demanda) + textoUnidades + " × " + textoFatorDemais + ")");
		}

		String aplicada = "g = (" + FormatoMemorial.numero(dmMax) + " × " + textoFatorMaior + ")";
		if (!termosDemais.isEmpty()) {
			aplicada += " + " + String.join(" + ", termosDemais);
		}

		contexto.registrar(ChaveResultado.G, g);
		PassoCalculo.Builder passo = passo(contexto, formula, aplicada, g, UnidadeMedida.KVA)
				.condicao("Maior motor: " + FormatoMemorial.numero(motores.get(indiceMaior).potenciaCv()) + " cv = "
						+ FormatoMemorial.numero(dmMax) + " kVA, uma unidade com fator " + textoFatorMaior
						+ "; demais unidades com fator " + textoFatorDemais + ". " + ORIGEM_DA_REGRA);
		for (int i = 0; i < motores.size(); i++) {
			int j = i + 1;
			passo.entrada("Potência do motor " + j, motores.get(i).potenciaCv(), UnidadeMedida.CV)
					.entrada("Quantidade do motor " + j, quantidade(motores.get(i).quantidade()), UnidadeMedida.ADIMENSIONAL)
					.entrada("Demanda do motor " + j, demandas.get(i), UnidadeMedida.KVA);
		}
		passo.referenciaNormativa(Tabela19MotoresCvKva.referencia());
		citar(passo, ParametroCalculo.FATOR_MAIOR_MOTOR, ParametroCalculo.FATOR_DEMAIS_MOTORES);
		contexto.memoria().adicionar(passo.build());
	}

}
