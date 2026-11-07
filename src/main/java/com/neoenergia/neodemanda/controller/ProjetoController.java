package com.neoenergia.neodemanda.controller;

import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.neoenergia.neodemanda.dto.ProjetoRequestDTO;
import com.neoenergia.neodemanda.dto.ProjetoResponseDTO;
import com.neoenergia.neodemanda.exception.ApiError;
import com.neoenergia.neodemanda.model.enums.StatusProjeto;
import com.neoenergia.neodemanda.service.ProjetoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
    @Operation(summary = "Listar projetos para o dashboard",
            description = "Retorna os projetos cadastrados e seus status. Sem filtro, os projetos arquivados ficam de fora; "
                    + "com o filtro, retorna so os projetos no status informado, inclusive ARQUIVADO.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de projetos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ProjetoResponseDTO.class)),
                            examples = @ExampleObject(name = "Projetos em analise", value = AvaliacaoExemplos.RESPOSTA_LISTA))),
            @ApiResponse(responseCode = "400", description = "Status invalido",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(name = "Status invalido", value = AvaliacaoExemplos.ERRO_400_STATUS)))
    })
    public ResponseEntity<List<ProjetoResponseDTO>> listarProjetos(
            @Parameter(description = "Filtro opcional por status") @RequestParam(required = false) StatusProjeto status) {
        List<ProjetoResponseDTO> projetos = projetoService.listar(status);
        return ResponseEntity.ok(projetos);
    }

	@GetMapping("/{id}")
	@Operation(summary = "Consulta o detalhe de um projeto",
			description = "Devolve os dados do projeto com o status atual, em qualquer status.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Projeto encontrado",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ProjetoResponseDTO.class),
							examples = @ExampleObject(name = "Projeto em rascunho", value = AvaliacaoExemplos.RESPOSTA_PROJETO))),
			@ApiResponse(responseCode = "404", description = "Projeto nao encontrado",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class),
							examples = @ExampleObject(name = "Projeto inexistente", value = AvaliacaoExemplos.ERRO_404_PROJETO)))
	})
	public ResponseEntity<ProjetoResponseDTO> consultar(@PathVariable Long id) {
		return ResponseEntity.ok(projetoService.consultar(id));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Edita um projeto existente",
			description = "Atualiza os dados de um projeto em RASCUNHO ou EM_CORRECAO. Id, data de criacao e status nao sao alterados.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Projeto atualizado",
					content = @Content(schema = @Schema(implementation = ProjetoResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados invalidos ou corpo mal formatado; o campo fields detalha cada violacao",
					content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Projeto nao encontrado",
					content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "409", description = "Projeto submetido nao pode ser editado",
					content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	public ResponseEntity<ProjetoResponseDTO> atualizar(@PathVariable Long id,
			@Valid @RequestBody ProjetoRequestDTO requisicao) {
		return ResponseEntity.ok(projetoService.atualizar(id, requisicao));
	}

	@PatchMapping("/{id}/arquivar")
	@Operation(summary = "Arquiva um projeto",
			description = "Muda o status do projeto para ARQUIVADO, removendo-o da listagem do dashboard.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Projeto arquivado",
					content = @Content(schema = @Schema(implementation = ProjetoResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Projeto nao encontrado",
					content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "409", description = "Projeto ja esta arquivado",
					content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	public ResponseEntity<ProjetoResponseDTO> arquivar(@PathVariable Long id) {
		return ResponseEntity.ok(projetoService.arquivar(id));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui um projeto",
			description = "Remove um projeto em RASCUNHO. Projetos submetidos ou arquivados nao podem ser excluidos.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Projeto excluido"),
			@ApiResponse(responseCode = "404", description = "Projeto nao encontrado",
					content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "409", description = "Projeto submetido ou arquivado nao pode ser excluido",
					content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		projetoService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
