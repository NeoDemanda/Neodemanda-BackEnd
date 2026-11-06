package com.neoenergia.neodemanda.motor.norma;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.motor.excecao.CoberturaNormativaException;
import com.neoenergia.neodemanda.motor.saida.ReferenciaNormativa;

final class SuporteNormas {

	private SuporteNormas() {
	}

	static ReferenciaNormativa disNor053(String item) {
		return new ReferenciaNormativa("DIS-NOR-053", "Rev. 06", item);
	}

	static ReferenciaNormativa disNor030(String item) {
		return new ReferenciaNormativa("DIS-NOR-030", "Rev. 07", item);
	}

	static BigDecimal valorOpcional(String valor) {
		return valor == null ? null : new BigDecimal(valor);
	}

	static boolean dentroDoLimite(BigDecimal valor, BigDecimal limiteSuperior) {
		return valor.signum() >= 0 && (limiteSuperior == null || valor.compareTo(limiteSuperior) <= 0);
	}

	static String texto(BigDecimal valor) {
		return valor.toPlainString().replace('.', ',');
	}

	static CoberturaNormativaException semCobertura(ReferenciaNormativa referencia, String valorProcurado) {
		return new CoberturaNormativaException(referencia.item() + " (" + referencia.norma() + " " + referencia.revisao()
				+ ") não contém " + valorProcurado + ".");
	}

}
