package com.neoenergia.neodemanda.dto;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.motor.entrada.UnidadePotenciaAr;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CondicionadorArRequest(

		@NotNull(message = "A potencia do condicionador e obrigatoria")
		@Positive(message = "A potencia do condicionador deve ser maior que zero")
		@Digits(integer = CalculoRequest.DIGITOS_INTEIROS, fraction = CalculoRequest.DIGITOS_FRACAO,
				message = "A potencia do condicionador " + CalculoRequest.LIMITE_DIGITOS)
		BigDecimal potencia,

		@NotNull(message = "A unidade de potencia e obrigatoria")
		UnidadePotenciaAr unidadePotencia,

		@NotNull(message = "A quantidade e obrigatoria")
		@Positive(message = "A quantidade deve ser maior que zero")
		@Max(value = CalculoRequest.QUANTIDADE_MAXIMA, message = CalculoRequest.LIMITE_QUANTIDADE)
		@Digits(integer = CalculoRequest.DIGITOS_QUANTIDADE, fraction = 0, message = CalculoRequest.QUANTIDADE_INTEIRA)
		@Schema(type = "integer", implementation = Integer.class)
		BigDecimal quantidade) {

	public CondicionadorArRequest {
		quantidade = CalculoRequest.normalizarQuantidade(quantidade);
	}

}
