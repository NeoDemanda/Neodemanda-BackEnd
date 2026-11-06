package com.neoenergia.neodemanda.motor.norma;

import java.math.BigDecimal;
import java.util.Arrays;

import com.neoenergia.neodemanda.motor.saida.ReferenciaNormativa;

public enum Quadro37FatorSeguranca {

	ATE_25("Dr ≤ 25 kVA", null, "25", "1.5"),
	DE_25_A_50("25 < Dr ≤ 50 kVA", "25", "50", "1.3"),
	DE_50_A_100("50 < Dr ≤ 100 kVA", "50", "100", "1.2"),
	ACIMA_DE_100("Dr > 100 kVA", "100", null, "1.1");

	private static final ReferenciaNormativa REFERENCIA = SuporteNormas.disNor053("Quadro 37");

	private final String faixa;

	private final BigDecimal limiteInferior;

	private final BigDecimal limiteSuperior;

	private final BigDecimal fator;

	Quadro37FatorSeguranca(String faixa, String limiteInferior, String limiteSuperior, String fator) {
		this.faixa = faixa;
		this.limiteInferior = SuporteNormas.valorOpcional(limiteInferior);
		this.limiteSuperior = SuporteNormas.valorOpcional(limiteSuperior);
		this.fator = new BigDecimal(fator);
	}

	public static ReferenciaNormativa referencia() {
		return REFERENCIA;
	}

	public static Quadro37FatorSeguranca porDemandaResidencial(BigDecimal drKva) {
		return Arrays.stream(values())
				.filter(linha -> SuporteNormas.dentroDoLimite(drKva, linha.limiteSuperior))
				.findFirst()
				.orElseThrow(() -> SuporteNormas.semCobertura(REFERENCIA,
						"fator para Dr de " + SuporteNormas.texto(drKva) + " kVA"));
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

	public BigDecimal fator() {
		return fator;
	}

}
