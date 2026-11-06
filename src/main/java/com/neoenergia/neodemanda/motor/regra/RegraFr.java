package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.norma.Quadro37FatorSeguranca;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraFr extends RegraDemanda {

	RegraFr() {
		super("FR", "Fator de segurança", "Fr", Quadro37FatorSeguranca.referencia());
	}

	@Override
	public boolean aplicavel(ContextoCalculo contexto) {
		return !contexto.entrada().apartamentos().isEmpty();
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		BigDecimal dr = contexto.obter(ChaveResultado.DR);
		Quadro37FatorSeguranca linha = Quadro37FatorSeguranca.porDemandaResidencial(dr);
		String textoDr = FormatoMemorial.calculado(dr);
		contexto.registrar(ChaveResultado.FR, linha.fator());
		contexto.memoria().adicionar(passo(contexto, "Fr = Quadro 37(Dr)", "Fr = Quadro 37(" + textoDr + ")",
				linha.fator(), UnidadeMedida.ADIMENSIONAL)
				.condicao("Dr = " + textoDr + " kVA → Quadro 37, faixa " + linha.faixa() + ": Fr = "
						+ FormatoMemorial.numero(linha.fator()))
				.entrada("Dr", dr, UnidadeMedida.KVA)
				.referenciaNormativa(Quadro37FatorSeguranca.referencia())
				.build());
	}

}
