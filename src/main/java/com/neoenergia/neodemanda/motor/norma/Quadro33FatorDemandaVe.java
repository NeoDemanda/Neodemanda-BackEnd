package com.neoenergia.neodemanda.motor.norma;

import java.math.BigDecimal;
import java.util.Arrays;

import com.neoenergia.neodemanda.motor.saida.ReferenciaNormativa;

public enum Quadro33FatorDemandaVe {

	DE_1_A_10("1 a 10 estações", 1, 10, "1.00"),
	DE_11_A_20("11 a 20 estações", 11, 20, "0.86"),
	DE_21_A_30("21 a 30 estações", 21, 30, "0.80"),
	DE_31_A_40("31 a 40 estações", 31, 40, "0.78"),
	DE_41_A_50("41 a 50 estações", 41, 50, "0.75"),
	DE_51_A_75("51 a 75 estações", 51, 75, "0.70"),
	DE_76_A_100("76 a 100 estações", 76, 100, "0.65"),
	ACIMA_DE_100("Acima de 100 estações", 101, null, "0.60");

	private static final ReferenciaNormativa REFERENCIA = SuporteNormas.disNor053("Quadro 33");

	private final String faixa;

	private final int minimo;

	private final Integer maximo;

	private final BigDecimal fator;

	Quadro33FatorDemandaVe(String faixa, int minimo, Integer maximo, String fator) {
		this.faixa = faixa;
		this.minimo = minimo;
		this.maximo = maximo;
		this.fator = new BigDecimal(fator);
	}

	public static ReferenciaNormativa referencia() {
		return REFERENCIA;
	}

	public static Quadro33FatorDemandaVe porEstacoes(int quantidade) {
		return Arrays.stream(values())
				.filter(linha -> quantidade >= linha.minimo && (linha.maximo == null || quantidade <= linha.maximo))
				.findFirst()
				.orElseThrow(() -> SuporteNormas.semCobertura(REFERENCIA, "fator para " + quantidade + " estações"));
	}

	public String faixa() {
		return faixa;
	}

	public int minimo() {
		return minimo;
	}

	public Integer maximo() {
		return maximo;
	}

	public BigDecimal fator() {
		return fator;
	}

}
