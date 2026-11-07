package com.neoenergia.neodemanda.controller;

final class AvaliacaoExemplos {

	static final String REQUISICAO_APROVADO = """
			{
			  "decisao": "APROVADO"
			}
			""";

	static final String REQUISICAO_EM_CORRECAO = """
			{
			  "decisao": "EM_CORRECAO",
			  "parecer": "Revisar a carga instalada informada no projeto."
			}
			""";

	static final String REQUISICAO_REPROVADO = """
			{
			  "decisao": "REPROVADO",
			  "parecer": "O projeto nao atende aos requisitos da norma."
			}
			""";

	static final String RESPOSTA_PROJETO = """
			{
			  "id": 1,
			  "nomeProjeto": "Edificio Aurora",
			  "responsavelTecnico": "Maria Silva",
			  "creaResponsavel": "PE-123456789",
			  "quantidadeUnidadesConsumidoras": 20,
			  "cargaInstaladaKva": 180.500,
			  "tipoEdificacao": "RESIDENCIAL",
			  "status": "RASCUNHO"
			}
			""";

	static final String RESPOSTA_LISTA = """
			[
			  {
			    "id": 1,
			    "nomeProjeto": "Edificio Aurora",
			    "responsavelTecnico": "Maria Silva",
			    "creaResponsavel": "PE-123456789",
			    "quantidadeUnidadesConsumidoras": 20,
			    "cargaInstaladaKva": 180.500,
			    "tipoEdificacao": "RESIDENCIAL",
			    "status": "EM_ANALISE"
			  }
			]
			""";

	static final String RESPOSTA_SUBMISSAO = """
			{
			  "id": 1,
			  "nomeProjeto": "Edificio Aurora",
			  "responsavelTecnico": "Maria Silva",
			  "creaResponsavel": "PE-123456789",
			  "quantidadeUnidadesConsumidoras": 20,
			  "cargaInstaladaKva": 180.500,
			  "tipoEdificacao": "RESIDENCIAL",
			  "status": "EM_ANALISE"
			}
			""";

	static final String RESPOSTA_AVALIACAO = """
			{
			  "projeto": {
			    "id": 1,
			    "nomeProjeto": "Edificio Aurora",
			    "responsavelTecnico": "Maria Silva",
			    "creaResponsavel": "PE-123456789",
			    "quantidadeUnidadesConsumidoras": 20,
			    "cargaInstaladaKva": 180.500,
			    "tipoEdificacao": "RESIDENCIAL",
			    "status": "EM_CORRECAO"
			  },
			  "avaliacao": {
			    "decisao": "EM_CORRECAO",
			    "parecer": "Revisar a carga instalada informada no projeto.",
			    "avaliadoEm": "2026-11-06T18:47:56.9478682-03:00"
			  }
			}
			""";

	static final String RESPOSTA_HISTORICO = """
			[
			  {
			    "decisao": "EM_CORRECAO",
			    "parecer": "Revisar a carga instalada informada no projeto.",
			    "avaliadoEm": "2026-11-06T18:47:56.9478682-03:00"
			  },
			  {
			    "decisao": "APROVADO",
			    "parecer": null,
			    "avaliadoEm": "2026-11-07T09:15:02.1034410-03:00"
			  }
			]
			""";

	static final String ERRO_400_PARECER = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 400,
			  "error": "Bad Request",
			  "message": "Erro de validacao nos dados enviados",
			  "path": "/projetos/1/avaliacao",
			  "fields": {
			    "parecer": "O parecer e obrigatorio para as decisoes EM_CORRECAO e REPROVADO"
			  }
			}
			""";

	static final String ERRO_400_PARECER_LIMITE = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 400,
			  "error": "Bad Request",
			  "message": "Erro de validacao nos dados enviados",
			  "path": "/projetos/1/avaliacao",
			  "fields": {
			    "parecer": "O parecer deve ter no maximo 2000 caracteres"
			  }
			}
			""";

	static final String ERRO_400_DECISAO = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 400,
			  "error": "Bad Request",
			  "message": "Erro de validacao nos dados enviados",
			  "path": "/projetos/1/avaliacao",
			  "fields": {
			    "decisao": "Valor invalido: ARQUIVADO"
			  }
			}
			""";

	static final String ERRO_400_STATUS = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 400,
			  "error": "Bad Request",
			  "message": "Erro de validacao nos dados enviados",
			  "path": "/projetos",
			  "fields": {
			    "status": "Valor invalido: PENDENTE"
			  }
			}
			""";

	static final String ERRO_404_PROJETO = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 404,
			  "error": "Not Found",
			  "message": "Projeto nao encontrado: 1",
			  "path": "/projetos/1",
			  "fields": {}
			}
			""";

	static final String ERRO_409_SEM_CALCULO = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 409,
			  "error": "Conflict",
			  "message": "Projeto sem calculo valido. Calcule a demanda antes de submeter.",
			  "path": "/projetos/1/submissao",
			  "fields": {}
			}
			""";

	static final String ERRO_409_SUBMISSAO = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 409,
			  "error": "Conflict",
			  "message": "Transicao invalida: projeto em APROVADO nao pode passar para EM_ANALISE",
			  "path": "/projetos/1/submissao",
			  "fields": {}
			}
			""";

	static final String ERRO_409_AVALIACAO = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 409,
			  "error": "Conflict",
			  "message": "Transicao invalida: projeto em RASCUNHO nao pode passar para APROVADO",
			  "path": "/projetos/1/avaliacao",
			  "fields": {}
			}
			""";

	private AvaliacaoExemplos() {
	}

}
