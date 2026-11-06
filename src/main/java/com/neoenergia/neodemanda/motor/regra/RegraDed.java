package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.norma.ReferenciaTermo;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraDed extends RegraDemanda {

	RegraDed() {
		super("DED", "Demanda total da edificação", "Ded", ReferenciaTermo.DED.referencia());
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		BigDecimal drf = contexto.obter(ChaveResultado.DRF);
		BigDecimal ds = contexto.obter(ChaveResultado.DS);
		BigDecimal dc = contexto.obter(ChaveResultado.DC);
		BigDecimal dve = contexto.obter(ChaveResultado.DVE);
		BigDecimal ded = drf.add(ds).add(dc).add(dve);
		contexto.registrar(ChaveResultado.DEMANDA_TOTAL, ded);
		contexto.memoria().adicionar(passo(contexto, "Ded = Drf + Ds + Dc + Dve",
				"Ded = " + FormatoMemorial.calculado(drf) + " + " + FormatoMemorial.calculado(ds) + " + "
						+ FormatoMemorial.calculado(dc) + " + " + FormatoMemorial.calculado(dve),
				ded, UnidadeMedida.KVA)
				.entrada("Drf", drf, UnidadeMedida.KVA)
				.entrada("Ds", ds, UnidadeMedida.KVA)
				.entrada("Dc", dc, UnidadeMedida.KVA)
				.entrada("Dve", dve, UnidadeMedida.KVA)
				.referenciaNormativa(ReferenciaTermo.DED.referencia())
				.build());
	}

}
