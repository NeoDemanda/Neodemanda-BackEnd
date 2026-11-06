package com.neoenergia.neodemanda.motor.entrada;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ApartamentoEntrada(

		@NotNull(message = "é obrigatória")
		@Positive(message = "deve ser maior que zero")
		BigDecimal areaUtilM2,

		@NotNull(message = "é obrigatória")
		@Positive(message = "deve ser maior que zero")
		Integer quantidade) {

}
