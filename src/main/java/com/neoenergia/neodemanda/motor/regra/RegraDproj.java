package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.norma.ParametroCalculo;
import com.neoenergia.neodemanda.motor.saida.Enquadramento;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraDproj extends RegraDemanda {

	RegraDproj() {
		super("DPROJ", "Demanda de projeto do padrão de entrada", "Dproj",
				ParametroCalculo.PISO_DEMANDA_PROJETO_KVA.referencia());
	}

	@Override
	public boolean aplicavel(ContextoCalculo contexto) {
		return contexto.enquadramento() == Enquadramento.BAIXA_TENSAO;
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		BigDecimal ded = contexto.obter(ChaveResultado.DEMANDA_TOTAL);
		BigDecimal piso = ParametroCalculo.PISO_DEMANDA_PROJETO_KVA.valor();
		BigDecimal dproj = ded.max(piso);
		String textoDed = FormatoMemorial.calculado(ded);
		String textoPiso = FormatoMemorial.numero(piso);
		String condicao = ded.compareTo(piso) < 0
				? "Ded = " + textoDed + " kVA é inferior ao piso de " + textoPiso + " kVA: adota-se o piso"
				: "Ded = " + textoDed + " kVA não é inferior ao piso de " + textoPiso + " kVA";

		contexto.registrar(ChaveResultado.DPROJ, dproj);
		contexto.memoria().adicionar(passo(contexto, "Dproj = max(Ded, " + textoPiso + ")",
				"Dproj = max(" + textoDed + "; " + textoPiso + ")", dproj, UnidadeMedida.KVA)
				.condicao(condicao)
				.entrada("Ded", ded, UnidadeMedida.KVA)
				.entrada("Piso do padrão de entrada", piso, UnidadeMedida.KVA)
				.referenciaNormativa(ParametroCalculo.PISO_DEMANDA_PROJETO_KVA.referencia())
				.build());
	}

}
