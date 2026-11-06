package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.entrada.ApartamentoEntrada;
import com.neoenergia.neodemanda.motor.norma.Quadro35DemandaPorArea;
import com.neoenergia.neodemanda.motor.saida.PassoCalculo;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraDr extends RegraDemanda {

	private static final String FORMULA = "Dr = ∑ (Dapto(Ak) × Nk)";

	RegraDr() {
		super("DR", "Demanda residencial bruta", "Dr", Quadro35DemandaPorArea.referencia());
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		List<ApartamentoEntrada> apartamentos = contexto.entrada().apartamentos();
		if (apartamentos.isEmpty()) {
			contexto.registrar(ChaveResultado.DR, BigDecimal.ZERO);
			contexto.memoria().adicionar(passo(contexto, FORMULA, "Dr = 0", BigDecimal.ZERO, UnidadeMedida.KVA)
					.condicao(SEM_APARTAMENTOS)
					.entrada("Ntotal", BigDecimal.ZERO, UnidadeMedida.ADIMENSIONAL)
					.referenciaNormativa(Quadro35DemandaPorArea.referencia())
					.build());
			return;
		}

		List<Quadro35DemandaPorArea> linhas = new ArrayList<>();
		List<String> termos = new ArrayList<>();
		List<String> faixas = new ArrayList<>();
		BigDecimal dr = BigDecimal.ZERO;
		for (ApartamentoEntrada apartamento : apartamentos) {
			Quadro35DemandaPorArea linha = Quadro35DemandaPorArea.porArea(apartamento.areaUtilM2());
			String demanda = FormatoMemorial.numero(linha.demandaKva());
			dr = dr.add(linha.demandaKva().multiply(quantidade(apartamento.quantidade())));
			linhas.add(linha);
			termos.add(demanda + " × " + apartamento.quantidade());
			faixas.add(FormatoMemorial.numero(apartamento.areaUtilM2()) + " m² → Quadro 35, faixa " + linha.faixa() + ": "
					+ demanda + " kVA");
		}

		contexto.registrar(ChaveResultado.DR, dr);
		PassoCalculo.Builder passo = passo(contexto, FORMULA, "Dr = " + String.join(" + ", termos), dr, UnidadeMedida.KVA)
				.condicao(String.join("; ", faixas))
				.referenciaNormativa(Quadro35DemandaPorArea.referencia());
		for (int i = 0; i < apartamentos.size(); i++) {
			int k = i + 1;
			ApartamentoEntrada apartamento = apartamentos.get(i);
			passo.entrada("A" + k, apartamento.areaUtilM2(), UnidadeMedida.M2)
					.entrada("N" + k, quantidade(apartamento.quantidade()), UnidadeMedida.ADIMENSIONAL)
					.entrada("Dapto(A" + k + ")", linhas.get(i).demandaKva(), UnidadeMedida.KVA);
		}
		contexto.memoria().adicionar(passo.build());
	}

}
