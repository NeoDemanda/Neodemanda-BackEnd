package com.neoenergia.neodemanda.model;

import java.time.OffsetDateTime;

import com.neoenergia.neodemanda.model.enums.DecisaoAvaliacao;

public record Avaliacao(
		Long projetoId,
		DecisaoAvaliacao decisao,
		String parecer,
		OffsetDateTime avaliadoEm) {

}
