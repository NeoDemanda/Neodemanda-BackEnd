package com.neoenergia.neodemanda.motor.norma;

import java.math.BigDecimal;
import java.util.Arrays;

import com.neoenergia.neodemanda.motor.saida.ReferenciaNormativa;

public enum Quadro35DemandaPorArea {

	ATE_40("Até 40 m²", null, "40", "1.00"),
	DE_41_A_45("41 a 45 m²", "41", "45", "1.05"),
	DE_46_A_50("46 a 50 m²", "46", "50", "1.16"),
	DE_51_A_55("51 a 55 m²", "51", "55", "1.26"),
	DE_56_A_60("56 a 60 m²", "56", "60", "1.36"),
	DE_61_A_65("61 a 65 m²", "61", "65", "1.47"),
	DE_66_A_70("66 a 70 m²", "66", "70", "1.57"),
	DE_71_A_75("71 a 75 m²", "71", "75", "1.67"),
	DE_76_A_80("76 a 80 m²", "76", "80", "1.76"),
	DE_81_A_85("81 a 85 m²", "81", "85", "1.86"),
	DE_86_A_90("86 a 90 m²", "86", "90", "1.96"),
	DE_91_A_95("91 a 95 m²", "91", "95", "2.06"),
	DE_96_A_100("96 a 100 m²", "96", "100", "2.16"),
	DE_101_A_110("101 a 110 m²", "101", "110", "2.35"),
	DE_111_A_120("111 a 120 m²", "111", "120", "2.54"),
	DE_121_A_130("121 a 130 m²", "121", "130", "2.73"),
	DE_131_A_140("131 a 140 m²", "131", "140", "2.91"),
	DE_141_A_150("141 a 150 m²", "141", "150", "3.06"),
	DE_151_A_160("151 a 160 m²", "151", "160", "3.28"),
	DE_161_A_170("161 a 170 m²", "161", "170", "3.47"),
	DE_171_A_180("171 a 180 m²", "171", "180", "3.65"),
	DE_181_A_190("181 a 190 m²", "181", "190", "3.83"),
	DE_191_A_200("191 a 200 m²", "191", "200", "4.01"),
	DE_201_A_220("201 a 220 m²", "201", "220", "4.36"),
	DE_221_A_240("221 a 240 m²", "221", "240", "4.72"),
	DE_241_A_260("241 a 260 m²", "241", "260", "5.07"),
	DE_261_A_280("261 a 280 m²", "261", "280", "5.42"),
	DE_281_A_300("281 a 300 m²", "281", "300", "5.76"),
	DE_301_A_350("301 a 350 m²", "301", "350", "6.61"),
	DE_351_A_400("351 a 400 m²", "351", "400", "7.45");

	private static final ReferenciaNormativa REFERENCIA = SuporteNormas.disNor053("Quadro 35");

	private final String faixa;

	private final BigDecimal limiteInferior;

	private final BigDecimal limiteSuperior;

	private final BigDecimal demandaKva;

	Quadro35DemandaPorArea(String faixa, String limiteInferior, String limiteSuperior, String demandaKva) {
		this.faixa = faixa;
		this.limiteInferior = SuporteNormas.valorOpcional(limiteInferior);
		this.limiteSuperior = new BigDecimal(limiteSuperior);
		this.demandaKva = new BigDecimal(demandaKva);
	}

	public static ReferenciaNormativa referencia() {
		return REFERENCIA;
	}

	public static Quadro35DemandaPorArea porArea(BigDecimal areaM2) {
		return Arrays.stream(values())
				.filter(linha -> SuporteNormas.dentroDoLimite(areaM2, linha.limiteSuperior))
				.findFirst()
				.orElseThrow(() -> SuporteNormas.semCobertura(REFERENCIA,
						"demanda para área de " + SuporteNormas.texto(areaM2) + " m²"));
	}

	public String faixa() {
		return faixa;
	}

	public BigDecimal limiteInferior() {
		return limiteInferior;
	}

	public BigDecimal limiteSuperior() {
		return limiteSuperior;
	}

	public BigDecimal demandaKva() {
		return demandaKva;
	}

}
