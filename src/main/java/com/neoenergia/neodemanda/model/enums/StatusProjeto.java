package com.neoenergia.neodemanda.model.enums;

/**
 * Situacao do projeto eletrico no fluxo de analise da distribuidora.
 *
 * <p>{@link #RASCUNHO} e o estado inicial: o projeto aceita edicao e ainda nao
 * foi submetido. {@link #EM_ANALISE} indica submissao concluida, aguardando
 * parecer. {@link #APROVADO} e {@link #REPROVADO} sao estados terminais do
 * parecer tecnico.
 */
public enum StatusProjeto {

	RASCUNHO,
	EM_ANALISE,
	APROVADO,
	REPROVADO

}
