package com.neoenergia.neodemanda.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CalculoRequest(

		Boolean condominioSmartStudio,

		List<@NotNull(message = "O item nao pode ser nulo") @Valid ApartamentoRequest> apartamentos,

		@PositiveOrZero(message = "A iluminacao das areas comuns nao pode ser negativa")
		@Digits(integer = CalculoRequest.DIGITOS_INTEIROS, fraction = CalculoRequest.DIGITOS_FRACAO,
				message = "A iluminacao das areas comuns " + CalculoRequest.LIMITE_DIGITOS)
		BigDecimal iluminacaoAreasComunsW,

		@PositiveOrZero(message = "As tomadas das areas comuns nao podem ser negativas")
		@Digits(integer = CalculoRequest.DIGITOS_INTEIROS, fraction = CalculoRequest.DIGITOS_FRACAO,
				message = "As tomadas das areas comuns " + CalculoRequest.LIMITE_DIGITOS)
		BigDecimal tomadasAreasComunsW,

		List<@NotNull(message = "O item nao pode ser nulo") @Valid MotorRequest> motores,

		@PositiveOrZero(message = "A carga comercial nao pode ser negativa")
		@Digits(integer = CalculoRequest.DIGITOS_INTEIROS, fraction = CalculoRequest.DIGITOS_FRACAO,
				message = "A carga comercial " + CalculoRequest.LIMITE_DIGITOS)
		BigDecimal cargaComercialKw,

		List<@NotNull(message = "O item nao pode ser nulo") @Valid CondicionadorArRequest> condicionadoresAr,

		List<@NotNull(message = "O item nao pode ser nulo") @Valid EstacaoRecargaRequest> estacoesRecarga,

		@NotNull(message = "O indicador de cliente do Grupo A e obrigatorio")
		Boolean clienteGrupoA) {

	public static final int DIGITOS_INTEIROS = 12;

	public static final int DIGITOS_FRACAO = 6;

	public static final int QUANTIDADE_MAXIMA = 1000000;

	static final int DIGITOS_QUANTIDADE = 7;

	static final String LIMITE_DIGITOS = "deve ter no maximo 12 digitos inteiros e 6 decimais";

	static final String LIMITE_QUANTIDADE = "A quantidade deve ser de no maximo 1000000";

	static final String QUANTIDADE_INTEIRA = "A quantidade deve ser um numero inteiro de ate 7 digitos";

	static BigDecimal normalizarQuantidade(BigDecimal quantidade) {
		return quantidade == null ? null : quantidade.stripTrailingZeros();
	}

}
