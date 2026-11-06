package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.norma.ReferenciaTermo;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraDs extends RegraDemanda {

	RegraDs() {
		super("DS", "Demanda de serviços do condomínio", "Ds", ReferenciaTermo.DS.referencia());
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		BigDecimal a = contexto.obter(ChaveResultado.A);
		BigDecimal g = contexto.obter(ChaveResultado.G);
		BigDecimal ds = a.add(g);
		contexto.registrar(ChaveResultado.DS, ds);
		contexto.memoria().adicionar(passo(contexto, "Ds = a + g",
				"Ds = " + FormatoMemorial.calculado(a) + " + " + FormatoMemorial.calculado(g), ds, UnidadeMedida.KVA)
				.entrada("a", a, UnidadeMedida.KVA)
				.entrada("g", g, UnidadeMedida.KVA)
				.referenciaNormativa(ReferenciaTermo.DS.referencia())
				.build());
	}

}
