package com.neoenergia.neodemanda.service;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.model.enums.TipoEdificacao;

public record ProjetoParaCalculo(
		TipoEdificacao tipoEdificacao,
		Integer quantidadeUnidadesConsumidoras,
		BigDecimal cargaInstaladaKva,
		long revisao) {

}
