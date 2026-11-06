package com.neoenergia.neodemanda.dto;

import com.neoenergia.neodemanda.model.enums.Perfil;

public record LoginResponse(
		Long id,
		String email,
		Perfil perfil) {

}
