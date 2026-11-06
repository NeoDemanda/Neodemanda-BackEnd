package com.neoenergia.neodemanda.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.neoenergia.neodemanda.model.Usuario;

@Repository
public class UsuarioRepository {

	private final AtomicLong sequenciaId = new AtomicLong();
	private final List<Usuario> usuariosEmMemoria = Collections.synchronizedList(new ArrayList<>());

	public Usuario salvar(Usuario usuario) {
		usuario.setId(sequenciaId.incrementAndGet());
		usuariosEmMemoria.add(usuario);
		return usuario;
	}

	public Optional<Usuario> buscarPorEmail(String email) {
		if (email == null) {
			return Optional.empty();
		}
		String emailNormalizado = normalizar(email);
		synchronized (usuariosEmMemoria) {
			return usuariosEmMemoria.stream()
					.filter(usuario -> normalizar(usuario.getEmail()).equals(emailNormalizado))
					.findFirst();
		}
	}

	private static String normalizar(String email) {
		return email.trim().toLowerCase(Locale.ROOT);
	}

}
