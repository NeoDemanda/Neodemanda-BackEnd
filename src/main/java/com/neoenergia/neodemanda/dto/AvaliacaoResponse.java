package com.neoenergia.neodemanda.dto;

import java.time.OffsetDateTime;

import com.neoenergia.neodemanda.model.Avaliacao;
import com.neoenergia.neodemanda.model.enums.DecisaoAvaliacao;

public record AvaliacaoResponse(
		DecisaoAvaliacao decisao,
		String parecer,
		OffsetDateTime avaliadoEm) {

	public static AvaliacaoResponse de(Avaliacao avaliacao) {
		return new AvaliacaoResponse(avaliacao.decisao(), avaliacao.parecer(), avaliacao.avaliadoEm());
	}

}
