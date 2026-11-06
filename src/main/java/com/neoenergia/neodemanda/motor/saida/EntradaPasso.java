package com.neoenergia.neodemanda.motor.saida;

import java.math.BigDecimal;
import java.util.Objects;

public record EntradaPasso(String nome, BigDecimal valor, UnidadeMedida unidade) {

	public EntradaPasso {
		PassoCalculo.exigirTexto(nome, "nome da entrada");
		Objects.requireNonNull(valor, "valor da entrada é obrigatório");
		Objects.requireNonNull(unidade, "unidade da entrada é obrigatória");
	}

}
