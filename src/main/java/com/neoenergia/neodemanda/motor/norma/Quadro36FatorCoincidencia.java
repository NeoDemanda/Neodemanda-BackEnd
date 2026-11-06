package com.neoenergia.neodemanda.motor.norma;

import java.math.BigDecimal;
import java.util.Arrays;

import com.neoenergia.neodemanda.motor.saida.ReferenciaNormativa;

public enum Quadro36FatorCoincidencia {

	APTOS_1("1 apto", 1, false, "1.00"),
	APTOS_2("2 aptos", 2, false, "0.9800"),
	APTOS_10("10 aptos", 10, false, "0.9640"),
	APTOS_20("20 aptos", 20, false, "0.8720"),
	APTOS_30("30 aptos", 30, false, "0.7827"),
	APTOS_40("40 aptos", 40, false, "0.7380"),
	APTOS_50("50 aptos", 50, false, "0.7068"),
	APTOS_60_OU_MAIS("60 ou mais aptos", 60, true, "0.6823");

	private static final ReferenciaNormativa REFERENCIA = SuporteNormas.disNor053("Quadro 36");

	private final String faixa;

	private final int apartamentos;

	private final boolean ouMais;

	private final BigDecimal fator;

	Quadro36FatorCoincidencia(String faixa, int apartamentos, boolean ouMais, String fator) {
		this.faixa = faixa;
		this.apartamentos = apartamentos;
		this.ouMais = ouMais;
		this.fator = new BigDecimal(fator);
	}

	public static ReferenciaNormativa referencia() {
		return REFERENCIA;
	}

	public static Quadro36FatorCoincidencia porApartamentos(int quantidade) {
		return Arrays.stream(values())
				.filter(linha -> linha.cobre(quantidade))
				.findFirst()
				.orElseThrow(() -> SuporteNormas.semCobertura(REFERENCIA, "fator para " + quantidade + " apartamentos"));
	}

	private boolean cobre(int quantidade) {
		return ouMais ? quantidade >= apartamentos : quantidade == apartamentos;
	}

	public String faixa() {
		return faixa;
	}

	public int apartamentos() {
		return apartamentos;
	}

	public boolean ouMais() {
		return ouMais;
	}

	public BigDecimal fator() {
		return fator;
	}

}
