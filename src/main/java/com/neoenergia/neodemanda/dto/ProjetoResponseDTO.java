package com.neoenergia.neodemanda.dto;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.model.enums.StatusProjeto;
import com.neoenergia.neodemanda.model.enums.TipoEdificacao;

/**
 * Representacao do projeto devolvida apos o cadastro: os dados recebidos, o
 * identificador atribuido e o {@link StatusProjeto} inicial.
 */
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
