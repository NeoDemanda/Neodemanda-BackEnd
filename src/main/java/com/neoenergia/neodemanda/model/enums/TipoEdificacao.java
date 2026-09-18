package com.neoenergia.neodemanda.model.enums;

/**
 * Classificacao da edificacao atendida pelo projeto eletrico.
 *
 * <p>O tipo de edificacao seleciona o conjunto de fatores de demanda aplicado
 * no calculo normativo, por isso vale como dado de entrada do projeto.
 *
 * <p><strong>ATENCAO - valores provisorios.</strong> Os tipos abaixo ainda nao
 * foram validados com a equipe tecnica da Neoenergia Pernambuco e devem mudar:
 * a norma pode exigir mais categorias (ex.: residencial coletivo x individual)
 * ou subdivisoes por faixa de carga. Nao trate esta lista como definitiva nem
 * persista os valores em producao antes da validacao.
 */
public enum TipoEdificacao {

	RESIDENCIAL,
	COMERCIAL,
	MISTO,
	INDUSTRIAL

}
