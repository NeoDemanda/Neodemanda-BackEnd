package com.neoenergia.neodemanda.motor.saida;

import java.math.BigDecimal;

public record ResultadoCalculo(
		BigDecimal demandaCalculada,
		MemoriaCalculo memoria) {

}
