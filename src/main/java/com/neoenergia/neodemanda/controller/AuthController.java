package com.neoenergia.neodemanda.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neoenergia.neodemanda.dto.LoginRequest;
import com.neoenergia.neodemanda.dto.LoginResponse;
import com.neoenergia.neodemanda.exception.ApiError;
import com.neoenergia.neodemanda.service.AutenticacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacao", description = "Login de usuarios")
public class AuthController {

	private final AutenticacaoService autenticacaoService;

	public AuthController(AutenticacaoService autenticacaoService) {
		this.autenticacaoService = autenticacaoService;
	}

	@PostMapping("/login")
	@Operation(summary = "Autentica um usuario",
			description = "Valida e-mail e senha e retorna id, e-mail e perfil do usuario.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Login realizado",
					content = @Content(schema = @Schema(implementation = LoginResponse.class))),
			@ApiResponse(responseCode = "400", description = "Dados invalidos ou corpo mal formatado; o campo fields detalha cada violacao",
					content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "401", description = "Usuario ou senha invalidos",
					content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest requisicao) {
		return ResponseEntity.ok(autenticacaoService.autenticar(requisicao));
	}

}
