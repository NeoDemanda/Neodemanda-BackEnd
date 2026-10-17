package com.neoenergia.neodemanda.motor;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.neoenergia.neodemanda.motor.saida.MemoriaCalculo;
import com.neoenergia.neodemanda.motor.saida.PassoCalculo;

class MemoriaCalculoTest {

	@Test
	@DisplayName("passos() não aceita modificação")
	void passosNaoAceitaModificacao() {
		MemoriaCalculo memoria = new MemoriaCalculo();
		memoria.adicionar(new PassoCalculo("TESTE", "passo", "TESTE", BigDecimal.ONE));

		List<PassoCalculo> passos = memoria.passos();
		PassoCalculo novo = new PassoCalculo("TESTE", "novo", "TESTE", BigDecimal.TEN);

		assertThrows(UnsupportedOperationException.class, () -> passos.add(novo));
		assertThrows(UnsupportedOperationException.class, () -> passos.remove(0));
		assertThrows(UnsupportedOperationException.class, passos::clear);
	}

}
