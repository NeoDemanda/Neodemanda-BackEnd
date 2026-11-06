package com.neoenergia.neodemanda.motor.norma;

import java.math.BigDecimal;
import java.util.Arrays;

import com.neoenergia.neodemanda.motor.saida.ReferenciaNormativa;

public enum Tabela19MotoresCvKva {

	CV_1("1", "1.52"),
	CV_2("2", "2.70"),
	CV_3("3", "4.04"),
	CV_5("5", "6.02"),
	CV_7_5("7.5", "8.65"),
	CV_10("10", "11.54"),
	CV_15("15", "16.65");

	private static final ReferenciaNormativa REFERENCIA = SuporteNormas.disNor030("Tabela 19");

	private final BigDecimal potenciaCv;

	private final BigDecimal demandaKva;

	Tabela19MotoresCvKva(String potenciaCv, String demandaKva) {
		this.potenciaCv = new BigDecimal(potenciaCv);
		this.demandaKva = new BigDecimal(demandaKva);
	}

	public static ReferenciaNormativa referencia() {
		return REFERENCIA;
	}

	public static Tabela19MotoresCvKva porPotenciaCv(BigDecimal potenciaCv) {
		return Arrays.stream(values())
				.filter(linha -> linha.potenciaCv.compareTo(potenciaCv) == 0)
				.findFirst()
				.orElseThrow(() -> SuporteNormas.semCobertura(REFERENCIA,
						"conversão para motor de " + SuporteNormas.texto(potenciaCv) + " cv"));
	}

	public BigDecimal potenciaCv() {
		return potenciaCv;
	}

	public BigDecimal demandaKva() {
		return demandaKva;
	}

}
