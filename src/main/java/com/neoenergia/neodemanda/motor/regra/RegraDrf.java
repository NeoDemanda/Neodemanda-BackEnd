package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.norma.ReferenciaTermo;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraDrf extends RegraDemanda {

	private static final String FORMULA = "Drf = Dtr × Fr";

	RegraDrf() {
		super("DRF", "Demanda residencial final", "Drf", ReferenciaTermo.DRF.referencia());
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		BigDecimal dtr = contexto.obter(ChaveResultado.DTR);
		if (contexto.entrada().apartamentos().isEmpty()) {
			contexto.registrar(ChaveResultado.DRF, BigDecimal.ZERO);
			contexto.memoria().adicionar(passo(contexto, FORMULA, "Drf = 0", BigDecimal.ZERO, UnidadeMedida.KVA)
					.condicao(SEM_APARTAMENTOS)
					.entrada("Dtr", dtr, UnidadeMedida.KVA)
					.referenciaNormativa(ReferenciaTermo.DRF.referencia())
					.build());
			return;
		}

		BigDecimal fr = contexto.obter(ChaveResultado.FR);
		BigDecimal drf = dtr.multiply(fr);
		contexto.registrar(ChaveResultado.DRF, drf);
		contexto.memoria().adicionar(passo(contexto, FORMULA,
				"Drf = " + FormatoMemorial.calculado(dtr) + " × " + FormatoMemorial.numero(fr), drf, UnidadeMedida.KVA)
				.entrada("Dtr", dtr, UnidadeMedida.KVA)
				.entrada("Fr", fr, UnidadeMedida.ADIMENSIONAL)
				.referenciaNormativa(ReferenciaTermo.DRF.referencia())
				.build());
	}

}
