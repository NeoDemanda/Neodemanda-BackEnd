package com.neoenergia.neodemanda.motor.saida;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.norma.Tabela2PadraoEntradaBt;

public record ResultadoCalculo(
		BigDecimal demandaCalculada,
		MemoriaCalculo memoria,
		BigDecimal dr,
		BigDecimal fc,
		BigDecimal dtr,
		BigDecimal fr,
		BigDecimal drf,
		BigDecimal a,
		BigDecimal g,
		BigDecimal ds,
		BigDecimal acom,
		BigDecimal far,
		BigDecimal dc,
		BigDecimal dve,
		Enquadramento enquadramento,
		BigDecimal dproj,
		Tabela2PadraoEntradaBt padraoEntrada) {

	public static ResultadoCalculo de(ContextoCalculo contexto) {
		return new ResultadoCalculo(
				contexto.obter(ChaveResultado.DEMANDA_TOTAL),
				contexto.memoria(),
				contexto.consultar(ChaveResultado.DR),
				contexto.consultar(ChaveResultado.FC),
				contexto.consultar(ChaveResultado.DTR),
				contexto.consultar(ChaveResultado.FR),
				contexto.consultar(ChaveResultado.DRF),
				contexto.consultar(ChaveResultado.A),
				contexto.consultar(ChaveResultado.G),
				contexto.consultar(ChaveResultado.DS),
				contexto.consultar(ChaveResultado.ACOM),
				contexto.consultar(ChaveResultado.FAR),
				contexto.consultar(ChaveResultado.DC),
				contexto.consultar(ChaveResultado.DVE),
				contexto.enquadramento(),
				contexto.consultar(ChaveResultado.DPROJ),
				contexto.padraoEntrada());
	}

	public BigDecimal ded() {
		return demandaCalculada;
	}

}
