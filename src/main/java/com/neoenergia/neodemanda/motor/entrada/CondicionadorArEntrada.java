package com.neoenergia.neodemanda.motor.entrada;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CondicionadorArEntrada(

		@NotNull(message = "é obrigatória")
		@Positive(message = "deve ser maior que zero")
		BigDecimal potencia,

		@NotNull(message = "é obrigatória")
		UnidadePotenciaAr unidadePotencia,

		@NotNull(message = "é obrigatória")
		@Positive(message = "deve ser maior que zero")
		Integer quantidade) {

}
