package com.neoenergia.neodemanda.model;

import com.neoenergia.neodemanda.model.enums.Perfil;

public class Usuario {

	private Long id;
	private String email;
	private String senhaHash;
	private Perfil perfil;

	public Usuario(String email, String senhaHash, Perfil perfil) {
		this.email = email;
		this.senhaHash = senhaHash;
		this.perfil = perfil;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public String getSenhaHash() {
		return senhaHash;
	}

	public Perfil getPerfil() {
		return perfil;
	}

}
