package com.neoenergia.neodemanda.dto;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.model.enums.StatusProjeto;
import com.neoenergia.neodemanda.model.enums.TipoEdificacao;


public record ProjetoResponseDTO(
		Long id,
		String nomeProjeto,
		String responsavelTecnico,
		String creaResponsavel,
		Integer quantidadeUnidadesConsumidoras,
		BigDecimal cargaInstaladaKva,
		TipoEdificacao tipoEdificacao,
		StatusProjeto status) {

}
