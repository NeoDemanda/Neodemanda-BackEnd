package com.neoenergia.neodemanda.motor.saida;

public record ReferenciaNormativa(String norma, String revisao, String item) {

	public ReferenciaNormativa {
		PassoCalculo.exigirTexto(norma, "norma");
		PassoCalculo.exigirTexto(revisao, "revisao");
		if (item != null && item.isBlank()) {
			throw new IllegalArgumentException("item não pode ficar em branco; use null quando a documentação não citar o item");
		}
	}

	public NormaRevisao normaRevisao() {
		return new NormaRevisao(norma, revisao);
	}

}
