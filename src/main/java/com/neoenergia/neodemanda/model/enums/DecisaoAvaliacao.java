package com.neoenergia.neodemanda.model.enums;

public enum DecisaoAvaliacao {

	APROVADO(StatusProjeto.APROVADO, false),
	EM_CORRECAO(StatusProjeto.EM_CORRECAO, true),
	REPROVADO(StatusProjeto.REPROVADO, true);

	private final StatusProjeto statusDestino;

	private final boolean exigeParecer;

	DecisaoAvaliacao(StatusProjeto statusDestino, boolean exigeParecer) {
		this.statusDestino = statusDestino;
		this.exigeParecer = exigeParecer;
	}

	public StatusProjeto statusDestino() {
		return statusDestino;
	}

	public boolean exigeParecer() {
		return exigeParecer;
	}

}
