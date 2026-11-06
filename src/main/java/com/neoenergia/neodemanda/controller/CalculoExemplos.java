package com.neoenergia.neodemanda.controller;

final class CalculoExemplos {

	static final String REQUISICAO_EXEMPLO_1 = """
			{
			  "condominioSmartStudio": false,
			  "apartamentos": [{ "areaUtilM2": 40, "quantidade": 20 }],
			  "iluminacaoAreasComunsW": 3000,
			  "tomadasAreasComunsW": 1500,
			  "motores": [{ "potenciaCv": 1, "quantidade": 1 }],
			  "cargaComercialKw": 0,
			  "condicionadoresAr": [],
			  "estacoesRecarga": [],
			  "clienteGrupoA": false
			}
			""";

	static final String RESPOSTA_EXEMPLO_1 = """
			{
			  "projetoId": 1,
			  "calculadoEm": "2026-11-06T18:47:56.7198311-03:00",
			  "entradas": {
			    "tipoEdificacao": "RESIDENCIAL",
			    "quantidadeUnidadesConsumidoras": 20,
			    "cargaInstaladaKva": 50,
			    "condominioSmartStudio": false,
			    "apartamentos": [{ "areaUtilM2": 40, "quantidade": 20 }],
			    "iluminacaoAreasComunsW": 3000,
			    "tomadasAreasComunsW": 1500,
			    "motores": [{ "potenciaCv": 1, "quantidade": 1 }],
			    "cargaComercialKw": 0,
			    "condicionadoresAr": [],
			    "estacoesRecarga": [],
			    "clienteGrupoA": false
			  },
			  "normas": [
			    { "norma": "DIS-NOR-053", "revisao": "Rev. 06" },
			    { "norma": "DIS-NOR-030", "revisao": "Rev. 07" }
			  ],
			  "resultado": {
			    "dr": 20.00, "fc": 0.8720, "dtr": 17.44, "fr": 1.5000, "drf": 26.16,
			    "a": 4.69, "g": 1.52, "ds": 6.21,
			    "acom": 0.00, "far": 0.00, "dc": 0.00, "dve": 0.00,
			    "ded": 32.37,
			    "enquadramento": "BAIXA_TENSAO",
			    "dproj": 46.00,
			    "padraoEntrada": {
			      "faixa": "Até 46 kVA",
			      "condutorPorFase": "1x 16 mm²",
			      "disjuntorA": 70.00,
			      "carcacaA": 160.00,
			      "eletroduto": "1x PEAD 110 mm"
			    }
			  },
			  "memorial": [
			    {
			      "ordem": 1, "idRegra": "DR", "simbolo": "Dr", "descricao": "Demanda residencial bruta",
			      "formulaSimbolica": "Dr = ∑ (Dapto(Ak) × Nk)",
			      "formulaAplicada": "Dr = 1,00 × 20",
			      "condicao": "40 m² → Quadro 35, faixa Até 40 m²: 1,00 kVA",
			      "entradas": [
			        { "nome": "A1", "valor": 40, "unidade": "M2" },
			        { "nome": "N1", "valor": 20, "unidade": "ADIMENSIONAL" },
			        { "nome": "Dapto(A1)", "valor": 1.00, "unidade": "KVA" }
			      ],
			      "valor": 20.00, "unidade": "KVA", "resultadoTexto": null,
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": "Quadro 35" }]
			    },
			    {
			      "ordem": 2, "idRegra": "FC", "simbolo": "Fc", "descricao": "Fator de coincidência",
			      "formulaSimbolica": "Fc = Quadro 36(Ntotal)",
			      "formulaAplicada": "Fc = Quadro 36(20)",
			      "condicao": "20 apartamentos → Quadro 36: 87,20%",
			      "entradas": [{ "nome": "Ntotal", "valor": 20, "unidade": "ADIMENSIONAL" }],
			      "valor": 0.8720, "unidade": "ADIMENSIONAL", "resultadoTexto": null,
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": "Quadro 36" }]
			    },
			    {
			      "ordem": 3, "idRegra": "DTR", "simbolo": "Dtr", "descricao": "Demanda residencial com coincidência",
			      "formulaSimbolica": "Dtr = Dr × Fc",
			      "formulaAplicada": "Dtr = 20 × 0,8720",
			      "condicao": null,
			      "entradas": [
			        { "nome": "Dr", "valor": 20.00, "unidade": "KVA" },
			        { "nome": "Fc", "valor": 0.8720, "unidade": "ADIMENSIONAL" }
			      ],
			      "valor": 17.44, "unidade": "KVA", "resultadoTexto": null,
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": null }]
			    },
			    {
			      "ordem": 4, "idRegra": "FR", "simbolo": "Fr", "descricao": "Fator de segurança",
			      "formulaSimbolica": "Fr = Quadro 37(Dr)",
			      "formulaAplicada": "Fr = Quadro 37(20)",
			      "condicao": "Dr = 20 kVA → Quadro 37, faixa Dr ≤ 25 kVA: Fr = 1,5",
			      "entradas": [{ "nome": "Dr", "valor": 20.00, "unidade": "KVA" }],
			      "valor": 1.5000, "unidade": "ADIMENSIONAL", "resultadoTexto": null,
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": "Quadro 37" }]
			    },
			    {
			      "ordem": 5, "idRegra": "DRF", "simbolo": "Drf", "descricao": "Demanda residencial final",
			      "formulaSimbolica": "Drf = Dtr × Fr",
			      "formulaAplicada": "Drf = 17,44 × 1,5",
			      "condicao": null,
			      "entradas": [
			        { "nome": "Dtr", "valor": 17.44, "unidade": "KVA" },
			        { "nome": "Fr", "valor": 1.5000, "unidade": "ADIMENSIONAL" }
			      ],
			      "valor": 26.16, "unidade": "KVA", "resultadoTexto": null,
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": "Seção 6.2" }]
			    },
			    {
			      "ordem": 6, "idRegra": "A", "simbolo": "a", "descricao": "Parcela de iluminação e tomadas de serviços",
			      "formulaSimbolica": "a = ( (Pilum / 1000) / FP × Fdi ) + ( (Ptug / 1000) / FP × Fdt )",
			      "formulaAplicada": "a = ( (3000 / 1000) / 0,80 × 1,00 ) + ( (1500 / 1000) / 0,80 × 0,50 )",
			      "condicao": null,
			      "entradas": [
			        { "nome": "Pilum", "valor": 3000, "unidade": "W" },
			        { "nome": "Ptug", "valor": 1500, "unidade": "W" },
			        { "nome": "FP", "valor": 0.8000, "unidade": "ADIMENSIONAL" },
			        { "nome": "Fdi", "valor": 1.0000, "unidade": "ADIMENSIONAL" },
			        { "nome": "Fdt", "valor": 0.5000, "unidade": "ADIMENSIONAL" }
			      ],
			      "valor": 4.69, "unidade": "KVA", "resultadoTexto": null,
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": null }]
			    },
			    {
			      "ordem": 7, "idRegra": "G", "simbolo": "g", "descricao": "Parcela de motores elétricos",
			      "formulaSimbolica": "g = (Dm_max × 1,00) + ∑ (Dm_j × 0,50)",
			      "formulaAplicada": "g = (1,52 × 1,00)",
			      "condicao": "Maior motor: 1 cv = 1,52 kVA, uma unidade com fator 1,00; demais unidades com fator 0,50. Regra de demanda de motores da Tabela 14; a norma dessa tabela não está identificada na documentação",
			      "entradas": [
			        { "nome": "Potência do motor 1", "valor": 1, "unidade": "CV" },
			        { "nome": "Quantidade do motor 1", "valor": 1, "unidade": "ADIMENSIONAL" },
			        { "nome": "Demanda do motor 1", "valor": 1.52, "unidade": "KVA" }
			      ],
			      "valor": 1.52, "unidade": "KVA", "resultadoTexto": null,
			      "referencias": [
			        { "norma": "DIS-NOR-030", "revisao": "Rev. 07", "item": "Tabela 19" },
			        { "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": "Seção 6.3" }
			      ]
			    },
			    {
			      "ordem": 8, "idRegra": "DS", "simbolo": "Ds", "descricao": "Demanda de serviços do condomínio",
			      "formulaSimbolica": "Ds = a + g",
			      "formulaAplicada": "Ds = 4,6875 + 1,52",
			      "condicao": null,
			      "entradas": [
			        { "nome": "a", "valor": 4.69, "unidade": "KVA" },
			        { "nome": "g", "valor": 1.52, "unidade": "KVA" }
			      ],
			      "valor": 6.21, "unidade": "KVA", "resultadoTexto": null,
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": "Seção 6.3" }]
			    },
			    {
			      "ordem": 9, "idRegra": "ACOM", "simbolo": "acom", "descricao": "Demanda de iluminação e tomadas comerciais",
			      "formulaSimbolica": "acom = ( (min(20, Pcom) / FP) × 1,00 ) + ( (max(0, Pcom - 20) / FP) × 0,70 )",
			      "formulaAplicada": "acom = 0",
			      "condicao": "Sem carga comercial",
			      "entradas": [
			        { "nome": "Pcom", "valor": 0, "unidade": "KW" },
			        { "nome": "FP", "valor": 0.8000, "unidade": "ADIMENSIONAL" },
			        { "nome": "Degrau comercial", "valor": 20, "unidade": "KW" },
			        { "nome": "Fator até o degrau", "valor": 1.0000, "unidade": "ADIMENSIONAL" },
			        { "nome": "Fator do excedente", "valor": 0.7000, "unidade": "ADIMENSIONAL" }
			      ],
			      "valor": 0.00, "unidade": "KVA", "resultadoTexto": null,
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": "Seção 6.4" }]
			    },
			    {
			      "ordem": 10, "idRegra": "FAR", "simbolo": "far", "descricao": "Demanda de ar-condicionado comercial",
			      "formulaSimbolica": "far = ∑ PAC × Fd_AC",
			      "formulaAplicada": "far = 0",
			      "condicao": "Sem condicionadores de ar",
			      "entradas": [{ "nome": "Quantidade de condicionadores", "valor": 0, "unidade": "ADIMENSIONAL" }],
			      "valor": 0.00, "unidade": "KVA", "resultadoTexto": null,
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": "Seção 6.4" }]
			    },
			    {
			      "ordem": 11, "idRegra": "DC", "simbolo": "Dc", "descricao": "Demanda comercial total",
			      "formulaSimbolica": "Dc = acom + far",
			      "formulaAplicada": "Dc = 0 + 0",
			      "condicao": null,
			      "entradas": [
			        { "nome": "acom", "valor": 0.00, "unidade": "KVA" },
			        { "nome": "far", "valor": 0.00, "unidade": "KVA" }
			      ],
			      "valor": 0.00, "unidade": "KVA", "resultadoTexto": null,
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": "Seção 6.4" }]
			    },
			    {
			      "ordem": 12, "idRegra": "DVE", "simbolo": "Dve", "descricao": "Demanda de recarga de veículos elétricos",
			      "formulaSimbolica": "Dve = ∑ Pve × Fd_VE",
			      "formulaAplicada": "Dve = 0",
			      "condicao": "Sem estações de recarga",
			      "entradas": [{ "nome": "Quantidade de estações", "valor": 0, "unidade": "ADIMENSIONAL" }],
			      "valor": 0.00, "unidade": "KVA", "resultadoTexto": null,
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": "Quadro 33" }]
			    },
			    {
			      "ordem": 13, "idRegra": "DED", "simbolo": "Ded", "descricao": "Demanda total da edificação",
			      "formulaSimbolica": "Ded = Drf + Ds + Dc + Dve",
			      "formulaAplicada": "Ded = 26,16 + 6,2075 + 0 + 0",
			      "condicao": null,
			      "entradas": [
			        { "nome": "Drf", "valor": 26.16, "unidade": "KVA" },
			        { "nome": "Ds", "valor": 6.21, "unidade": "KVA" },
			        { "nome": "Dc", "valor": 0.00, "unidade": "KVA" },
			        { "nome": "Dve", "valor": 0.00, "unidade": "KVA" }
			      ],
			      "valor": 32.37, "unidade": "KVA", "resultadoTexto": null,
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": "Seção 6.1" }]
			    },
			    {
			      "ordem": 14, "idRegra": "ENQUADRAMENTO", "simbolo": "Enquadramento", "descricao": "Enquadramento do atendimento",
			      "formulaSimbolica": "Cliente do Grupo A → média tensão; Ded ≤ 300 → baixa tensão; Ded > 300 → média tensão",
			      "formulaAplicada": "32,3675 ≤ 300 → baixa tensão",
			      "condicao": "Edificação residencial, sem cliente do Grupo A",
			      "entradas": [
			        { "nome": "Ded", "valor": 32.37, "unidade": "KVA" },
			        { "nome": "Limite de baixa tensão", "valor": 300.00, "unidade": "KVA" }
			      ],
			      "valor": 32.37, "unidade": "KVA", "resultadoTexto": "Baixa tensão",
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": null }]
			    },
			    {
			      "ordem": 15, "idRegra": "DPROJ", "simbolo": "Dproj", "descricao": "Demanda de projeto do padrão de entrada",
			      "formulaSimbolica": "Dproj = max(Ded, 46)",
			      "formulaAplicada": "Dproj = max(32,3675; 46)",
			      "condicao": "Ded = 32,3675 kVA é inferior ao piso de 46 kVA: adota-se o piso",
			      "entradas": [
			        { "nome": "Ded", "valor": 32.37, "unidade": "KVA" },
			        { "nome": "Piso do padrão de entrada", "valor": 46.00, "unidade": "KVA" }
			      ],
			      "valor": 46.00, "unidade": "KVA", "resultadoTexto": null,
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": "Tabela 2" }]
			    },
			    {
			      "ordem": 16, "idRegra": "PADRAO_ENTRADA", "simbolo": "Padrão de entrada", "descricao": "Padrão de entrada",
			      "formulaSimbolica": "Padrão de entrada = Tabela 2(Dproj)",
			      "formulaAplicada": "Padrão de entrada = Tabela 2(46)",
			      "condicao": "Dproj = 46 kVA → Tabela 2, faixa Até 46 kVA",
			      "entradas": [{ "nome": "Dproj", "valor": 46.00, "unidade": "KVA" }],
			      "valor": 46.00, "unidade": "KVA",
			      "resultadoTexto": "Condutor 1x 16 mm² por fase; disjuntor geral 70 A; carcaça 160 A; eletroduto 1x PEAD 110 mm",
			      "referencias": [{ "norma": "DIS-NOR-053", "revisao": "Rev. 06", "item": "Tabela 2" }]
			    }
			  ]
			}
			""";

	static final String ERRO_400_CAMPO = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 400,
			  "error": "Bad Request",
			  "message": "Erro de validacao nos dados enviados",
			  "path": "/projetos/1/calculo",
			  "fields": { "apartamentos[0].areaUtilM2": "A area util deve ser maior que zero" }
			}
			""";

	static final String ERRO_400_LIMITE = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 400,
			  "error": "Bad Request",
			  "message": "Erro de validacao nos dados enviados",
			  "path": "/projetos/1/calculo",
			  "fields": { "cargaComercialKw": "A carga comercial deve ter no maximo 12 digitos inteiros e 6 decimais" }
			}
			""";

	static final String ERRO_400_TIPO = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 400,
			  "error": "Bad Request",
			  "message": "Erro de validacao nos dados enviados",
			  "path": "/projetos/1/calculo",
			  "fields": { "tipoConexao": "Valor invalido: X" }
			}
			""";

	static final String ERRO_400_CORPO = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 400,
			  "error": "Bad Request",
			  "message": "Corpo da requisicao invalido ou mal formatado",
			  "path": "/projetos/1/calculo",
			  "fields": {}
			}
			""";

	static final String ERRO_400_DOMINIO = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 400,
			  "error": "Bad Request",
			  "message": "Edificação RESIDENCIAL não pode ter carga comercial (Pcom ou condicionadores de ar); use MISTO.",
			  "path": "/projetos/1/calculo",
			  "fields": {}
			}
			""";

	static final String ERRO_400_CARGA_PROJETO = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 400,
			  "error": "Bad Request",
			  "message": "A carga instalada do projeto esta fora do limite aceito para o calculo. Corrija o projeto.",
			  "path": "/projetos/1/calculo",
			  "fields": {}
			}
			""";

	static final String ERRO_404_PROJETO = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 404,
			  "error": "Not Found",
			  "message": "Projeto nao encontrado: 99",
			  "path": "/projetos/99/calculo",
			  "fields": {}
			}
			""";

	static final String ERRO_404_CALCULO = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 404,
			  "error": "Not Found",
			  "message": "Nenhum calculo valido para o projeto: 1",
			  "path": "/projetos/1/calculo",
			  "fields": {}
			}
			""";

	static final String ERRO_409_SUBMETIDO = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 409,
			  "error": "Conflict",
			  "message": "Projeto submetido nao pode ser calculado. Status atual: EM_ANALISE",
			  "path": "/projetos/1/calculo",
			  "fields": {}
			}
			""";

	static final String ERRO_409_ARQUIVADO = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 409,
			  "error": "Conflict",
			  "message": "Projeto arquivado nao pode ser calculado",
			  "path": "/projetos/1/calculo",
			  "fields": {}
			}
			""";

	static final String ERRO_422_QUADRO_36 = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 422,
			  "error": "Unprocessable Content",
			  "message": "Quadro 36 (DIS-NOR-053 Rev. 06) não contém fator para 15 apartamentos.",
			  "path": "/projetos/1/calculo",
			  "fields": {}
			}
			""";

	static final String ERRO_422_TABELA_11 = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 422,
			  "error": "Unprocessable Content",
			  "message": "Tabela 11 (DIS-NOR-030 Rev. 07) não está disponível na documentação: não é possível calcular a demanda dos condicionadores de ar informados.",
			  "path": "/projetos/1/calculo",
			  "fields": {}
			}
			""";

	static final String ERRO_422_INDUSTRIAL = """
			{
			  "timestamp": "2026-11-06T18:47:56.9478682-03:00",
			  "status": 422,
			  "error": "Unprocessable Content",
			  "message": "A documentação (DIS-NOR-053 Rev. 06) não cobre edificações industriais.",
			  "path": "/projetos/1/calculo",
			  "fields": {}
			}
			""";

	private CalculoExemplos() {
	}

}
