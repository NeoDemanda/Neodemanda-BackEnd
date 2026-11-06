package com.neoenergia.neodemanda.motor.saida;

public enum Enquadramento {

	BAIXA_TENSAO("Baixa tensão"),
	MEDIA_TENSAO("Média tensão");

	private final String descricao;

	Enquadramento(String descricao) {
		this.descricao = descricao;
	}

	public String descricao() {
		return descricao;
	}

}
