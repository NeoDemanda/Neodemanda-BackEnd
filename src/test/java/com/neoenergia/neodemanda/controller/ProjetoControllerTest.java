package com.neoenergia.neodemanda.controller;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.neoenergia.neodemanda.dto.ProjetoRequestDTO;
import com.neoenergia.neodemanda.dto.ProjetoResponseDTO;
import com.neoenergia.neodemanda.exception.ConflictException;
import com.neoenergia.neodemanda.exception.ResourceNotFoundException;
import com.neoenergia.neodemanda.model.enums.StatusProjeto;
import com.neoenergia.neodemanda.model.enums.TipoEdificacao;
import com.neoenergia.neodemanda.service.ProjetoService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjetoController.class)
class ProjetoControllerTest {

	private static final String PAYLOAD_VALIDO = """
			{
				"nomeProjeto": "Edificio Aurora",
				"responsavelTecnico": "Maria Silva",
				"creaResponsavel": "PE-123456789",
				"quantidadeUnidadesConsumidoras": 24,
				"cargaInstaladaKva": 180.500,
				"tipoEdificacao": "RESIDENCIAL"
			}
			""";

	private static final String PAYLOAD_INVALIDO = """
			{
				"nomeProjeto": "   ",
				"responsavelTecnico": "Maria Silva",
				"creaResponsavel": "PE-123456789",
				"quantidadeUnidadesConsumidoras": 0,
				"cargaInstaladaKva": 0,
				"tipoEdificacao": null
			}
			""";

	private static final String PAYLOAD_ENUM_INVALIDO = """
			{
				"nomeProjeto": "Edificio Aurora",
				"responsavelTecnico": "Maria Silva",
				"creaResponsavel": "PE-123456789",
				"quantidadeUnidadesConsumidoras": 24,
				"cargaInstaladaKva": 180.500,
				"tipoEdificacao": "RESIDENCIA"
			}
			""";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ProjetoService projetoService;

	@Test
	@DisplayName("POST /projetos com payload valido retorna 201 com Location e o projeto cadastrado")
	void cadastrarComPayloadValidoRetorna201() throws Exception {
		ProjetoResponseDTO resposta = new ProjetoResponseDTO(
				1L,
				"Edificio Aurora",
				"Maria Silva",
				"PE-123456789",
				24,
				new BigDecimal("180.500"),
				TipoEdificacao.RESIDENCIAL,
				StatusProjeto.RASCUNHO);
		given(projetoService.cadastrar(any(ProjetoRequestDTO.class))).willReturn(resposta);

		mockMvc.perform(post("/projetos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(PAYLOAD_VALIDO))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/projetos/1"))
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.nomeProjeto").value("Edificio Aurora"))
				.andExpect(jsonPath("$.cargaInstaladaKva").value(180.500))
				.andExpect(jsonPath("$.tipoEdificacao").value("RESIDENCIAL"))
				.andExpect(jsonPath("$.status").value("RASCUNHO"));

		verify(projetoService).cadastrar(any(ProjetoRequestDTO.class));
	}

	@Test
	@DisplayName("POST /projetos com payload invalido retorna 400 detalhando cada campo")
	void cadastrarComPayloadInvalidoRetorna400() throws Exception {
		mockMvc.perform(post("/projetos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(PAYLOAD_INVALIDO))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.fields.nomeProjeto").exists())
				.andExpect(jsonPath("$.fields.quantidadeUnidadesConsumidoras").exists())
				.andExpect(jsonPath("$.fields.cargaInstaladaKva").exists())
				.andExpect(jsonPath("$.fields.tipoEdificacao").exists());

		verifyNoInteractions(projetoService);
	}

	@Test
	@DisplayName("POST /projetos com tipoEdificacao fora do enum retorna 400 no formato ApiError")
	void cadastrarComEnumInvalidoRetorna400() throws Exception {
		mockMvc.perform(post("/projetos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(PAYLOAD_ENUM_INVALIDO))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.path").value("/projetos"))
				.andExpect(jsonPath("$.fields.tipoEdificacao").exists());

		verifyNoInteractions(projetoService);
	}

	@Test
	@DisplayName("PUT /projetos/{id} com payload valido retorna 200 com o projeto atualizado")
	void atualizarComPayloadValidoRetorna200() throws Exception {
		ProjetoResponseDTO resposta = new ProjetoResponseDTO(
				1L,
				"Edificio Aurora",
				"Maria Silva",
				"PE-123456789",
				24,
				new BigDecimal("180.500"),
				TipoEdificacao.RESIDENCIAL,
				StatusProjeto.RASCUNHO);
		given(projetoService.atualizar(eq(1L), any(ProjetoRequestDTO.class))).willReturn(resposta);

		mockMvc.perform(put("/projetos/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(PAYLOAD_VALIDO))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.nomeProjeto").value("Edificio Aurora"))
				.andExpect(jsonPath("$.cargaInstaladaKva").value(180.500))
				.andExpect(jsonPath("$.tipoEdificacao").value("RESIDENCIAL"))
				.andExpect(jsonPath("$.status").value("RASCUNHO"));

		verify(projetoService).atualizar(eq(1L), any(ProjetoRequestDTO.class));
	}

	@Test
	@DisplayName("PUT /projetos/{id} com id e status no corpo usa o id da URL e ignora os campos extras")
	void atualizarIgnoraIdEStatusDoCorpo() throws Exception {
		String payloadComIdEStatus = """
				{
					"id": 99,
					"status": "APROVADO",
					"dataCriacao": "2020-01-01T00:00:00",
					"nomeProjeto": "Edificio Aurora",
					"responsavelTecnico": "Maria Silva",
					"creaResponsavel": "PE-123456789",
					"quantidadeUnidadesConsumidoras": 24,
					"cargaInstaladaKva": 180.500,
					"tipoEdificacao": "RESIDENCIAL"
				}
				""";
		ProjetoResponseDTO resposta = new ProjetoResponseDTO(
				1L,
				"Edificio Aurora",
				"Maria Silva",
				"PE-123456789",
				24,
				new BigDecimal("180.500"),
				TipoEdificacao.RESIDENCIAL,
				StatusProjeto.RASCUNHO);
		given(projetoService.atualizar(eq(1L), any(ProjetoRequestDTO.class))).willReturn(resposta);

		mockMvc.perform(put("/projetos/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(payloadComIdEStatus))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.status").value("RASCUNHO"));

		verify(projetoService).atualizar(eq(1L), any(ProjetoRequestDTO.class));
	}

	@Test
	@DisplayName("PUT /projetos/{id} com payload invalido retorna 400 detalhando cada campo")
	void atualizarComPayloadInvalidoRetorna400() throws Exception {
		mockMvc.perform(put("/projetos/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(PAYLOAD_INVALIDO))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.path").value("/projetos/1"))
				.andExpect(jsonPath("$.fields.nomeProjeto").exists())
				.andExpect(jsonPath("$.fields.quantidadeUnidadesConsumidoras").exists())
				.andExpect(jsonPath("$.fields.cargaInstaladaKva").exists())
				.andExpect(jsonPath("$.fields.tipoEdificacao").exists());

		verifyNoInteractions(projetoService);
	}

	@Test
	@DisplayName("PUT /projetos/{id} de projeto inexistente retorna 404 no formato ApiError")
	void atualizarProjetoInexistenteRetorna404() throws Exception {
		given(projetoService.atualizar(eq(42L), any(ProjetoRequestDTO.class)))
				.willThrow(new ResourceNotFoundException("Projeto nao encontrado: 42"));

		mockMvc.perform(put("/projetos/42")
				.contentType(MediaType.APPLICATION_JSON)
				.content(PAYLOAD_VALIDO))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value("Projeto nao encontrado: 42"))
				.andExpect(jsonPath("$.path").value("/projetos/42"));
	}

	@Test
	@DisplayName("PUT /projetos/{id} de projeto submetido retorna 409 no formato ApiError")
	void atualizarProjetoSubmetidoRetorna409() throws Exception {
		given(projetoService.atualizar(eq(1L), any(ProjetoRequestDTO.class)))
				.willThrow(new ConflictException("Projeto submetido nao pode ser editado. Status atual: EM_ANALISE"));

		mockMvc.perform(put("/projetos/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(PAYLOAD_VALIDO))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409))
				.andExpect(jsonPath("$.message").value("Projeto submetido nao pode ser editado. Status atual: EM_ANALISE"))
				.andExpect(jsonPath("$.path").value("/projetos/1"));
	}

	@Test
	@DisplayName("PATCH /projetos/{id}/arquivar retorna 200 com o projeto arquivado")
	void arquivarRetorna200() throws Exception {
		ProjetoResponseDTO resposta = new ProjetoResponseDTO(
				1L,
				"Edificio Aurora",
				"Maria Silva",
				"PE-123456789",
				24,
				new BigDecimal("180.500"),
				TipoEdificacao.RESIDENCIAL,
				StatusProjeto.ARQUIVADO);
		given(projetoService.arquivar(1L)).willReturn(resposta);

		mockMvc.perform(patch("/projetos/1/arquivar"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.status").value("ARQUIVADO"));

		verify(projetoService).arquivar(1L);
	}

	@Test
	@DisplayName("PATCH /projetos/{id}/arquivar de projeto ja arquivado retorna 409 no formato ApiError")
	void arquivarProjetoJaArquivadoRetorna409() throws Exception {
		given(projetoService.arquivar(1L)).willThrow(new ConflictException("Projeto ja esta arquivado"));

		mockMvc.perform(patch("/projetos/1/arquivar"))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409))
				.andExpect(jsonPath("$.message").value("Projeto ja esta arquivado"))
				.andExpect(jsonPath("$.path").value("/projetos/1/arquivar"));
	}

	@Test
	@DisplayName("PATCH /projetos/{id}/arquivar de projeto inexistente retorna 404 no formato ApiError")
	void arquivarProjetoInexistenteRetorna404() throws Exception {
		given(projetoService.arquivar(42L)).willThrow(new ResourceNotFoundException("Projeto nao encontrado: 42"));

		mockMvc.perform(patch("/projetos/42/arquivar"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value("Projeto nao encontrado: 42"))
				.andExpect(jsonPath("$.path").value("/projetos/42/arquivar"));
	}

	@Test
	@DisplayName("DELETE /projetos/{id} retorna 204 sem corpo")
	void excluirRetorna204() throws Exception {
		mockMvc.perform(delete("/projetos/1"))
				.andExpect(status().isNoContent())
				.andExpect(content().string(""));

		verify(projetoService).excluir(1L);
	}

	@Test
	@DisplayName("DELETE /projetos/{id} de projeto submetido retorna 409 no formato ApiError")
	void excluirProjetoSubmetidoRetorna409() throws Exception {
		willThrow(new ConflictException("Projeto submetido nao pode ser excluido. Status atual: EM_ANALISE"))
				.given(projetoService).excluir(1L);

		mockMvc.perform(delete("/projetos/1"))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409))
				.andExpect(jsonPath("$.message").value("Projeto submetido nao pode ser excluido. Status atual: EM_ANALISE"))
				.andExpect(jsonPath("$.path").value("/projetos/1"));
	}

	@Test
	@DisplayName("DELETE /projetos/{id} de projeto inexistente retorna 404 no formato ApiError")
	void excluirProjetoInexistenteRetorna404() throws Exception {
		willThrow(new ResourceNotFoundException("Projeto nao encontrado: 42")).given(projetoService).excluir(42L);

		mockMvc.perform(delete("/projetos/42"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value("Projeto nao encontrado: 42"))
				.andExpect(jsonPath("$.path").value("/projetos/42"));
	}

}
