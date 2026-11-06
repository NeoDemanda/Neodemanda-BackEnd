package com.neoenergia.neodemanda.motor.entrada;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EstacaoRecargaEntrada(

		@NotNull(message = "é obrigatória")
		@Positive(message = "deve ser maior que zero")
		BigDecimal potenciaKw,

		@NotNull(message = "é obrigatória")
		@Positive(message = "deve ser maior que zero")
		Integer quantidade,

		@NotNull(message = "é obrigatório")
		TipoConexaoRecarga tipoConexao) {

}
