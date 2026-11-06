package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.norma.ParametroCalculo;
import com.neoenergia.neodemanda.motor.norma.Quadro36FatorCoincidencia;
import com.neoenergia.neodemanda.motor.saida.PassoCalculo;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraFc extends RegraDemanda {

	RegraFc() {
		super("FC", "Fator de coincidência", "Fc", Quadro36FatorCoincidencia.referencia());
	}

	@Override
	public boolean aplicavel(ContextoCalculo contexto) {
		return !contexto.entrada().apartamentos().isEmpty();
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		int ntotal = contexto.entrada().totalApartamentos();
		boolean smartStudio = contexto.entrada().condominioSmartStudio();
		BigDecimal limite = ParametroCalculo.LIMITE_UNIDADES_SMART_STUDIO.valor();

		if (smartStudio && quantidade(ntotal).compareTo(limite) > 0) {
			BigDecimal fc = ParametroCalculo.FC_SMART_STUDIO.valor();
			String textoLimite = FormatoMemorial.numero(limite);
			contexto.registrar(ChaveResultado.FC, fc);
			PassoCalculo.Builder passo = passo(contexto, "Fc = Fc Smart/Studio, se Ntotal > " + textoLimite,
					"Fc = " + FormatoMemorial.numero(fc), fc, UnidadeMedida.ADIMENSIONAL)
					.condicao("Smart/Studio com " + ntotal + " unidades (mais de " + textoLimite + "): Fc fixo de "
							+ FormatoMemorial.numero(fc))
					.entrada("Ntotal", quantidade(ntotal), UnidadeMedida.ADIMENSIONAL)
					.entrada("Limite de unidades Smart/Studio", limite, UnidadeMedida.ADIMENSIONAL);
			citar(passo, ParametroCalculo.FC_SMART_STUDIO, ParametroCalculo.LIMITE_UNIDADES_SMART_STUDIO);
			contexto.memoria().adicionar(passo.build());
			return;
		}

		Quadro36FatorCoincidencia linha = Quadro36FatorCoincidencia.porApartamentos(ntotal);
		String condicao = ntotal + " apartamentos → Quadro 36: " + FormatoMemorial.percentual(linha.fator());
		if (linha.ouMais()) {
			condicao += " (faixa " + linha.faixa() + ")";
		}
		if (smartStudio) {
			condicao = "Smart/Studio com até " + FormatoMemorial.numero(limite) + " unidades usa o Quadro 36; " + condicao;
		}
		contexto.registrar(ChaveResultado.FC, linha.fator());
		contexto.memoria().adicionar(passo(contexto, "Fc = Quadro 36(Ntotal)", "Fc = Quadro 36(" + ntotal + ")",
				linha.fator(), UnidadeMedida.ADIMENSIONAL)
				.condicao(condicao)
				.entrada("Ntotal", quantidade(ntotal), UnidadeMedida.ADIMENSIONAL)
				.referenciaNormativa(Quadro36FatorCoincidencia.referencia())
				.build());
	}

}
