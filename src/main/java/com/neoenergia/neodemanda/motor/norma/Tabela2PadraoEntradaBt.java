package com.neoenergia.neodemanda.motor.norma;

import java.math.BigDecimal;
import java.util.Arrays;

import com.neoenergia.neodemanda.motor.saida.ReferenciaNormativa;

public enum Tabela2PadraoEntradaBt {

	ATE_46("Até 46 kVA", null, "46", "1x 16 mm²", "70", "160", "1x PEAD 110 mm"),
	DE_46_A_53("46 a 53 kVA", "46", "53", "1x 25 mm²", "80", "160", "1x PEAD 110 mm"),
	DE_53_A_66("53 a 66 kVA", "53", "66", "1x 35 mm²", "100", "160", "1x PEAD 110 mm"),
	DE_66_A_82("66 a 82 kVA", "66", "82", "1x 50 mm²", "125", "160", "1x PEAD 110 mm"),
	DE_82_A_99("82 a 99 kVA", "82", "99", "1x 70 mm²", "150", "160", "1x PEAD 110 mm"),
	DE_99_A_135("99 a 135 kVA", "99", "135", "1x 95 mm²", "200", "250", "1x PEAD 110 mm"),
	DE_135_A_165("135 a 165 kVA", "135", "165", "1x 150 mm²", "250", "250", "1x PEAD 110 mm"),
	DE_165_A_211("165 a 211 kVA", "165", "211", "1x 240 mm²", "320", "400", "1x PEAD 110 mm"),
	DE_211_A_263("211 a 263 kVA", "211", "263", "2x 150 mm²", "400", "400", "2x PEAD 110 mm"),
	DE_263_A_300("263 a 300 kVA", "263", "300", "2x 185 mm²", "450", "630", "2x PEAD 110 mm");

	private static final ReferenciaNormativa REFERENCIA = SuporteNormas.disNor053("Tabela 2");

	private final String faixa;

	private final BigDecimal limiteInferior;

	private final BigDecimal limiteSuperior;

	private final String condutorPorFase;

	private final BigDecimal disjuntorA;

	private final BigDecimal carcacaA;

	private final String eletroduto;

	Tabela2PadraoEntradaBt(String faixa, String limiteInferior, String limiteSuperior, String condutorPorFase,
			String disjuntorA, String carcacaA, String eletroduto) {
		this.faixa = faixa;
		this.limiteInferior = SuporteNormas.valorOpcional(limiteInferior);
		this.limiteSuperior = new BigDecimal(limiteSuperior);
		this.condutorPorFase = condutorPorFase;
		this.disjuntorA = new BigDecimal(disjuntorA);
		this.carcacaA = new BigDecimal(carcacaA);
		this.eletroduto = eletroduto;
	}

	public static ReferenciaNormativa referencia() {
		return REFERENCIA;
	}

	public static Tabela2PadraoEntradaBt porDemandaProjeto(BigDecimal dprojKva) {
		return Arrays.stream(values())
				.filter(linha -> SuporteNormas.dentroDoLimite(dprojKva, linha.limiteSuperior))
				.findFirst()
				.orElseThrow(() -> SuporteNormas.semCobertura(REFERENCIA,
						"padrão de entrada para demanda de " + SuporteNormas.texto(dprojKva) + " kVA"));
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

	public String condutorPorFase() {
		return condutorPorFase;
	}

	public BigDecimal disjuntorA() {
		return disjuntorA;
	}

	public BigDecimal carcacaA() {
		return carcacaA;
	}

	public String eletroduto() {
		return eletroduto;
	}

}
