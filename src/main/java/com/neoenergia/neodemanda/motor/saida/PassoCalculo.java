package com.neoenergia.neodemanda.motor.saida;

import java.math.BigDecimal;

public record PassoCalculo(
		String idRegra,
		String descricao,
		String referenciaNormativa,
		BigDecimal valorProduzido) {

}
