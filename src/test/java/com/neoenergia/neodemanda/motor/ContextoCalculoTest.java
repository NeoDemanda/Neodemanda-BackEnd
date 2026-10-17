package com.neoenergia.neodemanda.motor;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.neoenergia.neodemanda.model.enums.TipoEdificacao;
import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.entrada.DadosEntradaCalculo;
import com.neoenergia.neodemanda.motor.excecao.RegraNaoAplicavelException;

class ContextoCalculoTest {

	@Test
	@DisplayName("obter de chave sem valor lança RegraNaoAplicavelException")
	void obterChaveAusenteLancaExcecao() {
		ContextoCalculo contexto = new ContextoCalculo(
				new DadosEntradaCalculo(TipoEdificacao.COMERCIAL, 4, new BigDecimal("80.00")));

		RegraNaoAplicavelException excecao = assertThrows(RegraNaoAplicavelException.class,
				() -> contexto.obter(ChaveResultado.DEMANDA_TOTAL));

		assertTrue(excecao.getMessage().contains(ChaveResultado.DEMANDA_TOTAL.name()));
	}

}
