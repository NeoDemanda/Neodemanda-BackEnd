package com.neoenergia.neodemanda.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.neoenergia.neodemanda.dto.LoginRequest;
import com.neoenergia.neodemanda.dto.LoginResponse;
import com.neoenergia.neodemanda.exception.CredenciaisInvalidasException;
import com.neoenergia.neodemanda.model.Usuario;
import com.neoenergia.neodemanda.model.enums.Perfil;
import com.neoenergia.neodemanda.repository.UsuarioRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AutenticacaoServiceTest {

	private static final String SENHA = "neodemanda123";

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private final UsuarioRepository usuarioRepository = new UsuarioRepository();
	private final AutenticacaoService autenticacaoService = new AutenticacaoService(usuarioRepository, passwordEncoder);

	private Usuario projetista;

	@BeforeEach
	void setUp() {
		projetista = usuarioRepository.salvar(
				new Usuario("projetista@neodemanda.local", passwordEncoder.encode(SENHA), Perfil.PROJETISTA));
	}

	@Test
	@DisplayName("autenticar com credenciais validas retorna id, email e perfil do usuario")
	void autenticarComCredenciaisValidas() {
		LoginResponse resposta = autenticacaoService.autenticar(new LoginRequest("projetista@neodemanda.local", SENHA));

		assertThat(resposta.id()).isEqualTo(projetista.getId());
		assertThat(resposta.email()).isEqualTo("projetista@neodemanda.local");
		assertThat(resposta.perfil()).isEqualTo(Perfil.PROJETISTA);
	}

	@Test
	@DisplayName("autenticar normaliza o email com trim e minusculas")
	void autenticarNormalizaEmail() {
		LoginResponse resposta = autenticacaoService
				.autenticar(new LoginRequest("  Projetista@NeoDemanda.LOCAL  ", SENHA));

		assertThat(resposta.id()).isEqualTo(projetista.getId());
	}

	@Test
	@DisplayName("autenticar com senha errada lanca CredenciaisInvalidasException")
	void autenticarComSenhaErrada() {
		assertThatThrownBy(() -> autenticacaoService
				.autenticar(new LoginRequest("projetista@neodemanda.local", "senha-errada")))
				.isInstanceOf(CredenciaisInvalidasException.class)
				.hasMessage("Usuário ou senha inválidos");
	}

	@Test
	@DisplayName("autenticar com email inexistente lanca CredenciaisInvalidasException com a mesma mensagem")
	void autenticarComEmailInexistente() {
		assertThatThrownBy(() -> autenticacaoService
				.autenticar(new LoginRequest("inexistente@neodemanda.local", SENHA)))
				.isInstanceOf(CredenciaisInvalidasException.class)
				.hasMessage("Usuário ou senha inválidos");
	}

}
