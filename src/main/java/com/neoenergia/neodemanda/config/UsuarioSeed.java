package com.neoenergia.neodemanda.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.neoenergia.neodemanda.model.Usuario;
import com.neoenergia.neodemanda.model.enums.Perfil;
import com.neoenergia.neodemanda.repository.UsuarioRepository;

@Component
public class UsuarioSeed implements ApplicationRunner {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final String senhaSeed;

	public UsuarioSeed(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
			@Value("${neodemanda.seed.senha}") String senhaSeed) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.senhaSeed = senhaSeed;
	}

	@Override
	public void run(ApplicationArguments args) {
		usuarioRepository.salvar(new Usuario("projetista@neodemanda.local", passwordEncoder.encode(senhaSeed),
				Perfil.PROJETISTA));
		usuarioRepository.salvar(new Usuario("analista@neodemanda.local", passwordEncoder.encode(senhaSeed),
				Perfil.ANALISTA));
	}

}
