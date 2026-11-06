package com.neoenergia.neodemanda.motor.norma;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.motor.saida.ReferenciaNormativa;

public enum ParametroCalculo {

	FATOR_POTENCIA("0.80", SuporteNormas.disNor053(null)),
	FATOR_DEMANDA_ILUMINACAO("1.00", SuporteNormas.disNor053(null)),
	FATOR_DEMANDA_TOMADAS("0.50", SuporteNormas.disNor053(null)),
	FATOR_MAIOR_MOTOR("1.00", SuporteNormas.disNor053("Seção 6.3")),
	FATOR_DEMAIS_MOTORES("0.50", SuporteNormas.disNor053("Seção 6.3")),
	FC_SMART_STUDIO("0.90", SuporteNormas.disNor053(null)),
	LIMITE_UNIDADES_SMART_STUDIO("15", SuporteNormas.disNor053(null)),
	DEGRAU_COMERCIAL_KW("20", SuporteNormas.disNor053("Seção 6.4")),
	FATOR_COMERCIAL_ATE_DEGRAU("1.00", SuporteNormas.disNor053("Seção 6.4")),
	FATOR_COMERCIAL_EXCEDENTE("0.70", SuporteNormas.disNor053("Seção 6.4")),
	PISO_DEMANDA_PROJETO_KVA("46", SuporteNormas.disNor053("Tabela 2")),
	LIMITE_BT_RESIDENCIAL_KVA("300", SuporteNormas.disNor053(null)),
	LIMITE_BT_COMERCIAL_MISTO_KVA("225", SuporteNormas.disNor053(null));

	private final BigDecimal valor;

	private final ReferenciaNormativa referencia;

	ParametroCalculo(String valor, ReferenciaNormativa referencia) {
		this.valor = new BigDecimal(valor);
		this.referencia = referencia;
	}

	public BigDecimal valor() {
		return valor;
	}

	public ReferenciaNormativa referencia() {
		return referencia;
	}

}
