package com.neoenergia.neodemanda.service;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.neoenergia.neodemanda.dto.LoginRequest;
import com.neoenergia.neodemanda.dto.LoginResponse;
import com.neoenergia.neodemanda.exception.CredenciaisInvalidasException;
import com.neoenergia.neodemanda.model.Usuario;
import com.neoenergia.neodemanda.repository.UsuarioRepository;

@Service
public class AutenticacaoService {

	public static final String MENSAGEM_CREDENCIAIS_INVALIDAS = "Usuário ou senha inválidos";

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	public AutenticacaoService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public LoginResponse autenticar(LoginRequest requisicao) {
		String emailNormalizado = requisicao.email().trim().toLowerCase(Locale.ROOT);

		Usuario usuario = usuarioRepository.buscarPorEmail(emailNormalizado)
				.filter(encontrado -> passwordEncoder.matches(requisicao.senha(), encontrado.getSenhaHash()))
				.orElseThrow(() -> new CredenciaisInvalidasException(MENSAGEM_CREDENCIAIS_INVALIDAS));

		return new LoginResponse(usuario.getId(), usuario.getEmail(), usuario.getPerfil());
	}

}
