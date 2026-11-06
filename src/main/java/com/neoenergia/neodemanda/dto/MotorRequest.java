package com.neoenergia.neodemanda.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MotorRequest(

		@NotNull(message = "A potencia do motor e obrigatoria")
		@Positive(message = "A potencia do motor deve ser maior que zero")
		@Digits(integer = CalculoRequest.DIGITOS_INTEIROS, fraction = CalculoRequest.DIGITOS_FRACAO,
				message = "A potencia do motor " + CalculoRequest.LIMITE_DIGITOS)
		BigDecimal potenciaCv,

		@NotNull(message = "A quantidade e obrigatoria")
		@Positive(message = "A quantidade deve ser maior que zero")
		@Max(value = CalculoRequest.QUANTIDADE_MAXIMA, message = CalculoRequest.LIMITE_QUANTIDADE)
		@Digits(integer = CalculoRequest.DIGITOS_QUANTIDADE, fraction = 0, message = CalculoRequest.QUANTIDADE_INTEIRA)
		@Schema(type = "integer", implementation = Integer.class)
		BigDecimal quantidade) {

	public MotorRequest {
		quantidade = CalculoRequest.normalizarQuantidade(quantidade);
	}

}
