package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.norma.ReferenciaTermo;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraDtr extends RegraDemanda {

	private static final String FORMULA = "Dtr = Dr × Fc";

	RegraDtr() {
		super("DTR", "Demanda residencial com coincidência", "Dtr", ReferenciaTermo.DTR.referencia());
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		BigDecimal dr = contexto.obter(ChaveResultado.DR);
		if (contexto.entrada().apartamentos().isEmpty()) {
			contexto.registrar(ChaveResultado.DTR, BigDecimal.ZERO);
			contexto.memoria().adicionar(passo(contexto, FORMULA, "Dtr = 0", BigDecimal.ZERO, UnidadeMedida.KVA)
					.condicao(SEM_APARTAMENTOS)
					.entrada("Dr", dr, UnidadeMedida.KVA)
					.referenciaNormativa(ReferenciaTermo.DTR.referencia())
					.build());
			return;
		}

		BigDecimal fc = contexto.obter(ChaveResultado.FC);
		BigDecimal dtr = dr.multiply(fc);
		contexto.registrar(ChaveResultado.DTR, dtr);
		contexto.memoria().adicionar(passo(contexto, FORMULA,
				"Dtr = " + FormatoMemorial.calculado(dr) + " × " + FormatoMemorial.numero(fc), dtr, UnidadeMedida.KVA)
				.entrada("Dr", dr, UnidadeMedida.KVA)
				.entrada("Fc", fc, UnidadeMedida.ADIMENSIONAL)
				.referenciaNormativa(ReferenciaTermo.DTR.referencia())
				.build());
	}

}
