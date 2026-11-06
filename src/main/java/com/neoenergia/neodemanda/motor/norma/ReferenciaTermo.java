package com.neoenergia.neodemanda.motor.norma;

import com.neoenergia.neodemanda.motor.saida.ReferenciaNormativa;

public enum ReferenciaTermo {

	DED(SuporteNormas.disNor053("Seção 6.1")),
	DRF(SuporteNormas.disNor053("Seção 6.2")),
	DTR(SuporteNormas.disNor053(null)),
	DS(SuporteNormas.disNor053("Seção 6.3")),
	DC(SuporteNormas.disNor053("Seção 6.4")),
	FAR(SuporteNormas.disNor053("Seção 6.4")),
	TABELA_11(SuporteNormas.disNor030("Tabela 11"));

	private final ReferenciaNormativa referencia;

	ReferenciaTermo(ReferenciaNormativa referencia) {
		this.referencia = referencia;
	}

	public ReferenciaNormativa referencia() {
		return referencia;
	}

	public String identificacao() {
		return referencia.item() + " (" + referencia.norma() + " " + referencia.revisao() + ")";
	}

}
