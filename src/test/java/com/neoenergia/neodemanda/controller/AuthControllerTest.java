package com.neoenergia.neodemanda.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.neoenergia.neodemanda.dto.LoginRequest;
import com.neoenergia.neodemanda.dto.LoginResponse;
import com.neoenergia.neodemanda.exception.CredenciaisInvalidasException;
import com.neoenergia.neodemanda.model.enums.Perfil;
import com.neoenergia.neodemanda.service.AutenticacaoService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

	private static final String PAYLOAD_VALIDO = """
			{
				"email": "projetista@neodemanda.local",
				"senha": "neodemanda123"
			}
			""";

	private static final String PAYLOAD_SENHA_ERRADA = """
			{
				"email": "projetista@neodemanda.local",
				"senha": "senha-errada"
			}
			""";

	private static final String PAYLOAD_EMAIL_INEXISTENTE = """
			{
				"email": "inexistente@neodemanda.local",
				"senha": "neodemanda123"
			}
			""";

	private static final String PAYLOAD_EMAIL_VAZIO = """
			{
				"email": "",
				"senha": "neodemanda123"
			}
			""";

	private static final String PAYLOAD_SENHA_VAZIA = """
			{
				"email": "projetista@neodemanda.local",
				"senha": ""
			}
			""";

	private static final String PAYLOAD_EMAIL_INVALIDO = """
			{
				"email": "email-invalido",
				"senha": "neodemanda123"
			}
			""";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AutenticacaoService autenticacaoService;

	@Test
	@DisplayName("POST /auth/login com credenciais validas retorna 200 com id, email e perfil, sem campo de senha")
	void loginValidoRetorna200() throws Exception {
		given(autenticacaoService.autenticar(any(LoginRequest.class)))
				.willReturn(new LoginResponse(1L, "projetista@neodemanda.local", Perfil.PROJETISTA));

		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(PAYLOAD_VALIDO))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.email").value("projetista@neodemanda.local"))
				.andExpect(jsonPath("$.perfil").value("PROJETISTA"))
				.andExpect(jsonPath("$.senha").doesNotExist())
				.andExpect(jsonPath("$.senhaHash").doesNotExist());

		verify(autenticacaoService).autenticar(any(LoginRequest.class));
	}

	@Test
	@DisplayName("POST /auth/login com senha errada retorna 401 com mensagem de credenciais invalidas")
	void loginComSenhaErradaRetorna401() throws Exception {
		given(autenticacaoService.autenticar(any(LoginRequest.class)))
				.willThrow(new CredenciaisInvalidasException("Usuário ou senha inválidos"));

		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(PAYLOAD_SENHA_ERRADA))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.status").value(401))
				.andExpect(jsonPath("$.message").value("Usuário ou senha inválidos"))
				.andExpect(jsonPath("$.path").value("/auth/login"));
	}

	@Test
	@DisplayName("POST /auth/login com email inexistente retorna 401 com a mesma mensagem")
	void loginComEmailInexistenteRetorna401() throws Exception {
		given(autenticacaoService.autenticar(any(LoginRequest.class)))
				.willThrow(new CredenciaisInvalidasException("Usuário ou senha inválidos"));

		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(PAYLOAD_EMAIL_INEXISTENTE))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.status").value(401))
				.andExpect(jsonPath("$.message").value("Usuário ou senha inválidos"))
				.andExpect(jsonPath("$.path").value("/auth/login"));
	}

	@Test
	@DisplayName("POST /auth/login com email vazio retorna 400 indicando o campo email")
	void loginComEmailVazioRetorna400() throws Exception {
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(PAYLOAD_EMAIL_VAZIO))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.fields.email").exists());

		verifyNoInteractions(autenticacaoService);
	}

	@Test
	@DisplayName("POST /auth/login com senha vazia retorna 400 indicando o campo senha")
	void loginComSenhaVaziaRetorna400() throws Exception {
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(PAYLOAD_SENHA_VAZIA))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.fields.senha").exists());

		verifyNoInteractions(autenticacaoService);
	}

	@Test
	@DisplayName("POST /auth/login com email mal formatado retorna 400 indicando o campo email")
	void loginComEmailMalFormatadoRetorna400() throws Exception {
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(PAYLOAD_EMAIL_INVALIDO))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.fields.email").exists());

		verifyNoInteractions(autenticacaoService);
	}

}
