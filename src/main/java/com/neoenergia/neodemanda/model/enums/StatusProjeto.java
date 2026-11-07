package com.neoenergia.neodemanda.model.enums;


public enum StatusProjeto {

	RASCUNHO,
	EM_ANALISE,
	EM_CORRECAO,
	APROVADO,
	REPROVADO,
	ARQUIVADO;

	public boolean podeTransicionarPara(StatusProjeto destino) {
		return switch (this) {
			case RASCUNHO, EM_CORRECAO -> destino == EM_ANALISE;
			case EM_ANALISE -> destino == APROVADO || destino == EM_CORRECAO || destino == REPROVADO;
			case APROVADO, REPROVADO, ARQUIVADO -> false;
		};
	}

}
