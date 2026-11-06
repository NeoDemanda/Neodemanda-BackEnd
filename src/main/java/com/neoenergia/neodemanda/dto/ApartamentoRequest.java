package com.neoenergia.neodemanda.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ApartamentoRequest(

		@NotNull(message = "A area util e obrigatoria")
		@Positive(message = "A area util deve ser maior que zero")
		@Digits(integer = CalculoRequest.DIGITOS_INTEIROS, fraction = CalculoRequest.DIGITOS_FRACAO,
				message = "A area util " + CalculoRequest.LIMITE_DIGITOS)
		BigDecimal areaUtilM2,

		@NotNull(message = "A quantidade e obrigatoria")
		@Positive(message = "A quantidade deve ser maior que zero")
		@Max(value = CalculoRequest.QUANTIDADE_MAXIMA, message = CalculoRequest.LIMITE_QUANTIDADE)
		@Digits(integer = CalculoRequest.DIGITOS_QUANTIDADE, fraction = 0, message = CalculoRequest.QUANTIDADE_INTEIRA)
		@Schema(type = "integer", implementation = Integer.class)
		BigDecimal quantidade) {

	public ApartamentoRequest {
		quantidade = CalculoRequest.normalizarQuantidade(quantidade);
	}

}
