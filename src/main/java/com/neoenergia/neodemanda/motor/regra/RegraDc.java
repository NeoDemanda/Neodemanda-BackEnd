package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.norma.ReferenciaTermo;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraDc extends RegraDemanda {

	RegraDc() {
		super("DC", "Demanda comercial total", "Dc", ReferenciaTermo.DC.referencia());
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		BigDecimal acom = contexto.obter(ChaveResultado.ACOM);
		BigDecimal far = contexto.obter(ChaveResultado.FAR);
		BigDecimal dc = acom.add(far);
		contexto.registrar(ChaveResultado.DC, dc);
		contexto.memoria().adicionar(passo(contexto, "Dc = acom + far",
				"Dc = " + FormatoMemorial.calculado(acom) + " + " + FormatoMemorial.calculado(far), dc, UnidadeMedida.KVA)
				.entrada("acom", acom, UnidadeMedida.KVA)
				.entrada("far", far, UnidadeMedida.KVA)
				.referenciaNormativa(ReferenciaTermo.DC.referencia())
				.build());
	}

}
