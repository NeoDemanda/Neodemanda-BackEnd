package com.neoenergia.neodemanda.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.test.util.ReflectionTestUtils;

import com.neoenergia.neodemanda.dto.ProjetoRequestDTO;
import com.neoenergia.neodemanda.dto.ProjetoResponseDTO;
import com.neoenergia.neodemanda.exception.ConflictException;
import com.neoenergia.neodemanda.exception.ResourceNotFoundException;
import com.neoenergia.neodemanda.model.Projeto;
import com.neoenergia.neodemanda.model.enums.StatusProjeto;
import com.neoenergia.neodemanda.model.enums.TipoEdificacao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjetoServiceTest {

	private final ProjetoService projetoService = new ProjetoService();

	private static final ProjetoRequestDTO REQUISICAO_ORIGINAL = new ProjetoRequestDTO(
			"Edificio Aurora",
			"Maria Silva",
			"PE-123456789",
			24,
			new BigDecimal("180.500"),
			TipoEdificacao.RESIDENCIAL);

	private static final ProjetoRequestDTO REQUISICAO_EDITADA = new ProjetoRequestDTO(
			"Edificio Aurora II",
			"Joao Souza",
			"PE-987654321",
			30,
			new BigDecimal("220.750"),
			TipoEdificacao.MISTO);

	@Test
	@DisplayName("atualizar() substitui os dados do projeto em RASCUNHO")
	void atualizarProjetoEmRascunho() {
		ProjetoResponseDTO cadastrado = projetoService.cadastrar(REQUISICAO_ORIGINAL);

		ProjetoResponseDTO atualizado = projetoService.atualizar(cadastrado.id(), REQUISICAO_EDITADA);

		assertThat(atualizado.nomeProjeto()).isEqualTo("Edificio Aurora II");
		assertThat(atualizado.responsavelTecnico()).isEqualTo("Joao Souza");
		assertThat(atualizado.creaResponsavel()).isEqualTo("PE-987654321");
		assertThat(atualizado.quantidadeUnidadesConsumidoras()).isEqualTo(30);
		assertThat(atualizado.cargaInstaladaKva()).isEqualByComparingTo("220.750");
		assertThat(atualizado.tipoEdificacao()).isEqualTo(TipoEdificacao.MISTO);
		assertThat(projetoService.listarTodos()).containsExactly(atualizado);
	}

	@Test
	@DisplayName("atualizar() mantem id, status e data de criacao do projeto")
	void atualizarMantemIdStatusEDataCriacao() {
		ProjetoResponseDTO cadastrado = projetoService.cadastrar(REQUISICAO_ORIGINAL);
		Projeto projeto = projetoEmMemoria(cadastrado.id());
		LocalDateTime dataCriacao = LocalDateTime.of(2026, 1, 10, 9, 30);
		projeto.setDataCriacao(dataCriacao);

		ProjetoResponseDTO atualizado = projetoService.atualizar(cadastrado.id(), REQUISICAO_EDITADA);

		assertThat(atualizado.id()).isEqualTo(cadastrado.id());
		assertThat(atualizado.status()).isEqualTo(StatusProjeto.RASCUNHO);
		assertThat(projeto.getDataCriacao()).isEqualTo(dataCriacao);
	}

	@Test
	@DisplayName("atualizar() de projeto inexistente lanca ResourceNotFoundException")
	void atualizarProjetoInexistente() {
		assertThatThrownBy(() -> projetoService.atualizar(42L, REQUISICAO_EDITADA))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("42");
	}

	@ParameterizedTest
	@EnumSource(value = StatusProjeto.class, names = { "EM_ANALISE", "APROVADO", "REPROVADO" })
	@DisplayName("atualizar() de projeto submetido lanca ConflictException e nao altera os dados")
	void atualizarProjetoSubmetido(StatusProjeto status) {
		ProjetoResponseDTO cadastrado = projetoService.cadastrar(REQUISICAO_ORIGINAL);
		projetoEmMemoria(cadastrado.id()).setStatus(status);

		assertThatThrownBy(() -> projetoService.atualizar(cadastrado.id(), REQUISICAO_EDITADA))
				.isInstanceOf(ConflictException.class)
				.hasMessageContaining("Projeto submetido nao pode ser editado");

		assertThat(projetoEmMemoria(cadastrado.id()).getNomeProjeto()).isEqualTo("Edificio Aurora");
	}

	@Test
	@DisplayName("atualizar() de projeto arquivado lanca ConflictException")
	void atualizarProjetoArquivado() {
		ProjetoResponseDTO cadastrado = projetoService.cadastrar(REQUISICAO_ORIGINAL);
		projetoService.arquivar(cadastrado.id());

		assertThatThrownBy(() -> projetoService.atualizar(cadastrado.id(), REQUISICAO_EDITADA))
				.isInstanceOf(ConflictException.class)
				.hasMessageContaining("Projeto arquivado nao pode ser editado");
	}

	@ParameterizedTest
	@EnumSource(value = StatusProjeto.class, names = "ARQUIVADO", mode = EnumSource.Mode.EXCLUDE)
	@DisplayName("arquivar() muda o status para ARQUIVADO a partir de qualquer outro status")
	void arquivarProjeto(StatusProjeto status) {
		ProjetoResponseDTO cadastrado = projetoService.cadastrar(REQUISICAO_ORIGINAL);
		projetoEmMemoria(cadastrado.id()).setStatus(status);

		ProjetoResponseDTO arquivado = projetoService.arquivar(cadastrado.id());

		assertThat(arquivado.id()).isEqualTo(cadastrado.id());
		assertThat(arquivado.status()).isEqualTo(StatusProjeto.ARQUIVADO);
	}

	@Test
	@DisplayName("arquivar() de projeto ja arquivado lanca ConflictException")
	void arquivarProjetoJaArquivado() {
		ProjetoResponseDTO cadastrado = projetoService.cadastrar(REQUISICAO_ORIGINAL);
		projetoService.arquivar(cadastrado.id());

		assertThatThrownBy(() -> projetoService.arquivar(cadastrado.id()))
				.isInstanceOf(ConflictException.class)
				.hasMessageContaining("Projeto ja esta arquivado");
	}

	@Test
	@DisplayName("arquivar() de projeto inexistente lanca ResourceNotFoundException")
	void arquivarProjetoInexistente() {
		assertThatThrownBy(() -> projetoService.arquivar(42L))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("42");
	}

	@Test
	@DisplayName("listarTodos() nao retorna projetos arquivados")
	void listarTodosOcultaArquivados() {
		ProjetoResponseDTO ativo = projetoService.cadastrar(REQUISICAO_ORIGINAL);
		ProjetoResponseDTO arquivado = projetoService.cadastrar(REQUISICAO_EDITADA);

		projetoService.arquivar(arquivado.id());

		assertThat(projetoService.listarTodos())
				.extracting(ProjetoResponseDTO::id)
				.containsExactly(ativo.id());
	}

	@Test
	@DisplayName("excluir() remove o projeto em RASCUNHO")
	void excluirProjetoEmRascunho() {
		ProjetoResponseDTO cadastrado = projetoService.cadastrar(REQUISICAO_ORIGINAL);

		projetoService.excluir(cadastrado.id());

		assertThat(projetoService.listarTodos()).isEmpty();
		assertThatThrownBy(() -> projetoService.excluir(cadastrado.id()))
				.isInstanceOf(ResourceNotFoundException.class);
	}

	@ParameterizedTest
	@EnumSource(value = StatusProjeto.class, names = { "EM_ANALISE", "APROVADO", "REPROVADO" })
	@DisplayName("excluir() de projeto submetido lanca ConflictException e mantem o projeto")
	void excluirProjetoSubmetido(StatusProjeto status) {
		ProjetoResponseDTO cadastrado = projetoService.cadastrar(REQUISICAO_ORIGINAL);
		projetoEmMemoria(cadastrado.id()).setStatus(status);

		assertThatThrownBy(() -> projetoService.excluir(cadastrado.id()))
				.isInstanceOf(ConflictException.class)
				.hasMessageContaining("Projeto submetido nao pode ser excluido");

		assertThat(projetoService.listarTodos()).extracting(ProjetoResponseDTO::id).containsExactly(cadastrado.id());
	}

	@Test
	@DisplayName("excluir() de projeto EM_ANALISE que foi arquivado lanca ConflictException")
	void excluirProjetoEmAnaliseArquivado() {
		ProjetoResponseDTO cadastrado = projetoService.cadastrar(REQUISICAO_ORIGINAL);
		projetoEmMemoria(cadastrado.id()).setStatus(StatusProjeto.EM_ANALISE);
		projetoService.arquivar(cadastrado.id());

		assertThatThrownBy(() -> projetoService.excluir(cadastrado.id()))
				.isInstanceOf(ConflictException.class)
				.hasMessageContaining("Projeto arquivado nao pode ser excluido");

		assertThat(projetoEmMemoria(cadastrado.id()).getStatus()).isEqualTo(StatusProjeto.ARQUIVADO);
	}

	@Test
	@DisplayName("excluir() de projeto inexistente lanca ResourceNotFoundException")
	void excluirProjetoInexistente() {
		assertThatThrownBy(() -> projetoService.excluir(42L))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("42");
	}

	@SuppressWarnings("unchecked")
	private Projeto projetoEmMemoria(Long id) {
		List<Projeto> projetos = (List<Projeto>) ReflectionTestUtils.getField(projetoService, "projetosEmMemoria");
		return projetos.stream()
				.filter(projeto -> projeto.getId().equals(id))
				.findFirst()
				.orElseThrow();
	}

}
