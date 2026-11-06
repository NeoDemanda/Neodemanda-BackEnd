package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;

final class FormatoMemorial {

	private FormatoMemorial() {
	}

	static String numero(BigDecimal valor) {
		return valor.toPlainString().replace('.', ',');
	}

	static String calculado(BigDecimal valor) {
		return numero(valor.stripTrailingZeros());
	}

	static String percentual(BigDecimal fator) {
		return numero(fator.movePointRight(2)) + "%";
	}

}
