package com.neoenergia.neodemanda.dto;

import com.neoenergia.neodemanda.model.enums.DecisaoAvaliacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@ParecerObrigatorio
public record AvaliacaoRequest(

		@NotNull(message = "A decisao e obrigatoria")
		DecisaoAvaliacao decisao,

		@Size(max = 2000, message = "O parecer deve ter no maximo 2000 caracteres")
		String parecer) {

	public AvaliacaoRequest {
		parecer = parecer == null || parecer.isBlank() ? null : parecer.strip();
	}

}
