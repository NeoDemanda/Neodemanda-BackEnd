package com.neoenergia.neodemanda.motor.entrada;

import java.math.BigDecimal;
import java.util.Objects;

import com.neoenergia.neodemanda.model.enums.TipoEdificacao;

public record DadosEntradaCalculo(
		TipoEdificacao tipoEdificacao,
		Integer quantidadeUnidadesConsumidoras,
		BigDecimal cargaInstaladaKva) {

	public DadosEntradaCalculo {
		Objects.requireNonNull(tipoEdificacao, "tipoEdificacao é obrigatório");
		Objects.requireNonNull(quantidadeUnidadesConsumidoras, "quantidadeUnidadesConsumidoras é obrigatório");
		Objects.requireNonNull(cargaInstaladaKva, "cargaInstaladaKva é obrigatório");
	}

}
