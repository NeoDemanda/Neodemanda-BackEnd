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
import com.neoenergia.neodemanda.model.enums.StatusProjeto;
import com.neoenergia.neodemanda.model.enums.TipoEdificacao;
import com.neoenergia.neodemanda.service.ProjetoService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}
