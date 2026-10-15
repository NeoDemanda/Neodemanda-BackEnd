package com.neoenergia.neodemanda.motor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.neoenergia.neodemanda.model.enums.TipoEdificacao;
import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.entrada.DadosEntradaCalculo;
import com.neoenergia.neodemanda.motor.excecao.EntradaInvalidaException;
import com.neoenergia.neodemanda.motor.regra.RegraCalculo;
import com.neoenergia.neodemanda.motor.saida.PassoCalculo;
import com.neoenergia.neodemanda.motor.saida.ResultadoCalculo;

class MotorCalculoTest {

	private final DadosEntradaCalculo entrada =
			new DadosEntradaCalculo(TipoEdificacao.RESIDENCIAL, 10, new BigDecimal("150.456"));

	private final List<String> execucoes = new ArrayList<>();

	@Test
	@DisplayName("regras executam na ordem recebida")
	void regrasExecutamNaOrdemRecebida() {
		MotorCalculo motor = new MotorCalculo(List.of(
				new RegraFicticia("primeira", true, execucoes),
				new RegraFicticia("segunda", true, execucoes),
				new RegraFicticia("terceira", true, execucoes)));

		motor.calcular(entrada);

		assertEquals(List.of("primeira", "segunda", "terceira"), execucoes);
	}

	@Test
	@DisplayName("regra com aplicavel false não executa nem registra passo")
	void regraNaoAplicavelNaoExecuta() {
		MotorCalculo motor = new MotorCalculo(List.of(
				new RegraFicticia("aplicavel", true, execucoes),
				new RegraFicticia("nao-aplicavel", false, execucoes)));

		ResultadoCalculo resultado = motor.calcular(entrada);

		assertFalse(execucoes.contains("nao-aplicavel"));
		assertEquals(List.of("aplicavel"), descricoes(resultado.memoria().passos()));
	}

	@Test
	@DisplayName("cada regra aplicada registra um passo, na ordem de execução")
	void cadaRegraAplicadaRegistraUmPasso() {
		MotorCalculo motor = new MotorCalculo(List.of(
				new RegraFicticia("primeira", true, execucoes),
				new RegraFicticia("ignorada", false, execucoes),
				new RegraFicticia("segunda", true, execucoes)));

		ResultadoCalculo resultado = motor.calcular(entrada);

		List<PassoCalculo> passos = resultado.memoria().passos();
		assertEquals(List.of("primeira", "segunda"), descricoes(passos));
		assertEquals(execucoes, descricoes(passos));
	}

	@Test
	@DisplayName("calcular com entrada nula lança EntradaInvalidaException")
	void entradaNulaLancaExcecao() {
		MotorCalculo motor = new MotorCalculo(List.of(new RegraFicticia("unica", true, execucoes)));

		assertThrows(EntradaInvalidaException.class, () -> motor.calcular(null));
	}

	@Test
	@DisplayName("a mesma entrada executada duas vezes produz o mesmo demandaCalculada")
	void mesmaEntradaProduzMesmoResultado() {
		MotorCalculo motor = new MotorCalculo(List.of(new RegraFicticia("unica", true, execucoes)));

		ResultadoCalculo primeiro = motor.calcular(entrada);
		ResultadoCalculo segundo = motor.calcular(entrada);

		assertEquals(primeiro.demandaCalculada(), segundo.demandaCalculada());
	}

	private static List<String> descricoes(List<PassoCalculo> passos) {
		return passos.stream().map(PassoCalculo::descricao).toList();
	}

	private static final class RegraFicticia implements RegraCalculo {

		private final String nome;

		private final boolean aplicavel;

		private final List<String> execucoes;

		RegraFicticia(String nome, boolean aplicavel, List<String> execucoes) {
			this.nome = nome;
			this.aplicavel = aplicavel;
			this.execucoes = execucoes;
		}

		@Override
		public String id() {
			return "TESTE";
		}

		@Override
		public String referenciaNormativa() {
			return "TESTE";
		}

		@Override
		public boolean aplicavel(ContextoCalculo contexto) {
			return aplicavel;
		}

		@Override
		public void aplicar(ContextoCalculo contexto) {
			execucoes.add(nome);
			BigDecimal valor = contexto.entrada().cargaInstaladaKva()
					.setScale(MotorConstantes.ESCALA, MotorConstantes.ARREDONDAMENTO);
			contexto.registrar(ChaveResultado.DEMANDA_TOTAL, valor);
			contexto.memoria().adicionar(new PassoCalculo(id(), nome, referenciaNormativa(), valor));
		}

	}

}
