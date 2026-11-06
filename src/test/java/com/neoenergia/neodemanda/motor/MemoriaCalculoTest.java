package com.neoenergia.neodemanda.motor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.neoenergia.neodemanda.motor.saida.MemoriaCalculo;
import com.neoenergia.neodemanda.motor.saida.NormaRevisao;
import com.neoenergia.neodemanda.motor.saida.PassoCalculo;
import com.neoenergia.neodemanda.motor.saida.ReferenciaNormativa;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

class MemoriaCalculoTest {

	private static final ReferenciaNormativa QUADRO_36 =
			new ReferenciaNormativa("DIS-NOR-053", "Rev. 06", "Quadro 36");

	private static final ReferenciaNormativa QUADRO_37 =
			new ReferenciaNormativa("DIS-NOR-053", "Rev. 06", "Quadro 37");

	private static final ReferenciaNormativa TABELA_19 =
			new ReferenciaNormativa("DIS-NOR-030", "Rev. 07", "Tabela 19");

	private static final ReferenciaNormativa NORMA_SEM_ITEM =
			new ReferenciaNormativa("DIS-NOR-053", "Rev. 06", null);

	private static final NormaRevisao DIS_NOR_053 = new NormaRevisao("DIS-NOR-053", "Rev. 06");

	private static final NormaRevisao DIS_NOR_030 = new NormaRevisao("DIS-NOR-030", "Rev. 07");

	@Test
	@DisplayName("passos() não aceita modificação")
	void passosNaoAceitaModificacao() {
		MemoriaCalculo memoria = new MemoriaCalculo();
		memoria.adicionar(passo(1, "passo", BigDecimal.ONE));

		List<PassoCalculo> passos = memoria.passos();
		PassoCalculo novo = passo(2, "novo", BigDecimal.TEN);

		assertThrows(UnsupportedOperationException.class, () -> passos.add(novo));
		assertThrows(UnsupportedOperationException.class, () -> passos.remove(0));
		assertThrows(UnsupportedOperationException.class, passos::clear);
	}

	@Test
	@DisplayName("passos ficam na ordem em que foram adicionados")
	void ordemPreservadaNaMemoria() {
		MemoriaCalculo memoria = new MemoriaCalculo();

		memoria.adicionar(passo(memoria.proximaOrdem(), "primeiro", BigDecimal.ONE));
		memoria.adicionar(passo(memoria.proximaOrdem(), "segundo", BigDecimal.ONE));
		memoria.adicionar(passo(memoria.proximaOrdem(), "terceiro", BigDecimal.ONE));

		assertEquals(List.of(1, 2, 3), memoria.passos().stream().map(PassoCalculo::ordem).toList());
		assertEquals(List.of("primeiro", "segundo", "terceiro"),
				memoria.passos().stream().map(PassoCalculo::descricao).toList());
	}

	@Test
	@DisplayName("passo com ordem fora de sequência é rejeitado com mensagem clara")
	void ordemForaDeSequenciaEhRejeitada() {
		MemoriaCalculo memoria = new MemoriaCalculo();
		memoria.adicionar(passo(1, "primeiro", BigDecimal.ONE));
		PassoCalculo foraDeSequencia = passo(3, "terceiro", BigDecimal.ONE);

		IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
				() -> memoria.adicionar(foraDeSequencia));

		assertEquals("ordem 3 fora de sequência; esperada 2", excecao.getMessage());
		assertEquals(1, memoria.passos().size());
	}

	@Test
	@DisplayName("adicionar passo nulo é rejeitado")
	void passoNuloEhRejeitado() {
		MemoriaCalculo memoria = new MemoriaCalculo();

		assertThrows(NullPointerException.class, () -> memoria.adicionar(null));
	}

	@Test
	@DisplayName("memória vazia não tem normas usadas")
	void memoriaVaziaNaoTemNormas() {
		assertEquals(List.of(), new MemoriaCalculo().normasUsadas());
	}

	@Test
	@DisplayName("normas usadas aparecem sem repetição, na ordem dos passos")
	void normasUsadasSemRepeticaoNaOrdemDosPassos() {
		MemoriaCalculo memoria = new MemoriaCalculo();
		memoria.adicionar(passoComReferencias(1, TABELA_19));
		memoria.adicionar(passoComReferencias(2, QUADRO_36));
		memoria.adicionar(passoComReferencias(3, QUADRO_37, TABELA_19));

		assertEquals(List.of(DIS_NOR_030, DIS_NOR_053), memoria.normasUsadas());
	}

	@Test
	@DisplayName("norma citada só como segunda referência de um passo entra nas normas usadas")
	void normaDaSegundaReferenciaEntraNasNormasUsadas() {
		MemoriaCalculo memoria = new MemoriaCalculo();
		memoria.adicionar(passoComReferencias(1, QUADRO_36, TABELA_19));

		assertEquals(List.of(DIS_NOR_053, DIS_NOR_030), memoria.normasUsadas());
	}

	@Test
	@DisplayName("referência sem item conta como a mesma norma e revisão")
	void referenciaSemItemNaoRepeteNorma() {
		MemoriaCalculo memoria = new MemoriaCalculo();
		memoria.adicionar(passoComReferencias(1, QUADRO_36));
		memoria.adicionar(passoComReferencias(2, NORMA_SEM_ITEM));

		assertEquals(List.of(DIS_NOR_053), memoria.normasUsadas());
	}

	@Test
	@DisplayName("normasUsadas() não aceita modificação")
	void normasUsadasNaoAceitaModificacao() {
		MemoriaCalculo memoria = new MemoriaCalculo();
		memoria.adicionar(passoComReferencias(1, QUADRO_36));

		List<NormaRevisao> normas = memoria.normasUsadas();

		assertThrows(UnsupportedOperationException.class, normas::clear);
	}

	private static PassoCalculo passo(int ordem, String descricao, BigDecimal valor) {
		return construtor(ordem)
				.descricao(descricao)
				.valorProduzido(valor)
				.referenciaNormativa(QUADRO_36)
				.build();
	}

	private static PassoCalculo passoComReferencias(int ordem, ReferenciaNormativa... referencias) {
		PassoCalculo.Builder builder = construtor(ordem).descricao("passo").valorProduzido(BigDecimal.ONE);
		for (ReferenciaNormativa referencia : referencias) {
			builder.referenciaNormativa(referencia);
		}
		return builder.build();
	}

	private static PassoCalculo.Builder construtor(int ordem) {
		return PassoCalculo.builder()
				.ordem(ordem)
				.idRegra("TESTE")
				.simbolo("TESTE")
				.formulaSimbolica("TESTE")
				.formulaAplicada("TESTE")
				.entrada("entrada", BigDecimal.ONE, UnidadeMedida.ADIMENSIONAL)
				.unidade(UnidadeMedida.KVA);
	}

}
