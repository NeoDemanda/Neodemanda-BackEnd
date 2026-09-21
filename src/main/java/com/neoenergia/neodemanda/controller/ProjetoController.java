package com.neoenergia.neodemanda.controller;

import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.neoenergia.neodemanda.dto.ProjetoRequestDTO;
import com.neoenergia.neodemanda.dto.ProjetoResponseDTO;
import com.neoenergia.neodemanda.exception.ApiError;
import com.neoenergia.neodemanda.service.ProjetoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Endpoints de projeto eletrico.
 */
@RestController
@RequestMapping("/projetos")
@Tag(name = "Projetos", description = "Cadastro e consulta de projetos eletricos de edificacao")
public class ProjetoController {

	private final ProjetoService projetoService;

	public ProjetoController(ProjetoService projetoService) {
		this.projetoService = projetoService;
	}

	@PostMapping
	@Operation(summary = "Cadastra um novo projeto eletrico",
			description = "Recebe os dados do formulario de Novo Projeto e registra o projeto com status RASCUNHO. "
					+ "A demanda calculada nao e informada aqui: e resultado do calculo normativo.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Projeto cadastrado; o header Location aponta para o recurso criado",
					content = @Content(schema = @Schema(implementation = ProjetoResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados invalidos ou corpo mal formatado; o campo fields detalha cada violacao",
					content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	public ResponseEntity<ProjetoResponseDTO> cadastrar(@Valid @RequestBody ProjetoRequestDTO requisicao) {
		ProjetoResponseDTO projetoCadastrado = projetoService.cadastrar(requisicao);

		URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(projetoCadastrado.id())
				.toUri();

		return ResponseEntity.created(localizacao).body(projetoCadastrado);
	}

	@GetMapping
    @Operation(summary = "Listar projetos para o dashboard", description = "Retorna a lista de todos os projetos cadastrados e seus status.")
    public ResponseEntity<List<ProjetoResponseDTO>> listarProjetos() {
        List<ProjetoResponseDTO> projetos = projetoService.listarTodos();
        return ResponseEntity.ok(projetos);
    }
}
