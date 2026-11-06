package com.neoenergia.neodemanda.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

		@NotBlank(message = "O e-mail e obrigatorio")
		@Email(message = "O e-mail deve ser valido")
		String email,

		@NotBlank(message = "A senha e obrigatoria")
		@Size(max = 72, message = "A senha deve ter no maximo 72 caracteres")
		String senha) {

	@Override
	public String toString() {
		return "LoginRequest[email=" + email + ", senha=***]";
	}

}
