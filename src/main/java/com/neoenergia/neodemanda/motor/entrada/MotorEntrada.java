package com.neoenergia.neodemanda.motor.entrada;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MotorEntrada(

		@NotNull(message = "é obrigatória")
		@Positive(message = "deve ser maior que zero")
		BigDecimal potenciaCv,

		@NotNull(message = "é obrigatória")
		@Positive(message = "deve ser maior que zero")
		Integer quantidade) {

}
