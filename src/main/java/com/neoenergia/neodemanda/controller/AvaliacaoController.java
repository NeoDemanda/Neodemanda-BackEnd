package com.neoenergia.neodemanda.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neoenergia.neodemanda.dto.AvaliacaoRequest;
import com.neoenergia.neodemanda.dto.AvaliacaoResponse;
import com.neoenergia.neodemanda.dto.ProjetoAvaliadoResponse;
import com.neoenergia.neodemanda.dto.ProjetoResponseDTO;
import com.neoenergia.neodemanda.exception.ApiError;
import com.neoenergia.neodemanda.service.AvaliacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/projetos/{id}")
@Tag(name = "Avaliacao de projetos", description = "Submissao de projetos, avaliacao e historico de avaliacoes")
public class AvaliacaoController {

	private final AvaliacaoService avaliacaoService;

	public AvaliacaoController(AvaliacaoService avaliacaoService) {
		this.avaliacaoService = avaliacaoService;
	}

	@PostMapping("/submissao")
	@Operation(summary = "Submete um projeto para avaliacao",
			description = "Move um projeto em RASCUNHO ou EM_CORRECAO para EM_ANALISE. Nao tem corpo. "
					+ "Exige um calculo valido guardado: editar o projeto invalida o calculo.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Projeto submetido",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ProjetoResponseDTO.class),
							examples = @ExampleObject(name = "Projeto submetido", value = AvaliacaoExemplos.RESPOSTA_SUBMISSAO))),
			@ApiResponse(responseCode = "404", description = "Projeto nao encontrado",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class),
							examples = @ExampleObject(name = "Projeto inexistente", value = AvaliacaoExemplos.ERRO_404_PROJETO))),
			@ApiResponse(responseCode = "409", description = "Projeto sem calculo valido ou status que nao permite a submissao",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class),
							examples = {
									@ExampleObject(name = "Sem calculo valido", value = AvaliacaoExemplos.ERRO_409_SEM_CALCULO),
									@ExampleObject(name = "Transicao invalida", value = AvaliacaoExemplos.ERRO_409_SUBMISSAO)
							}))
	})
	public ResponseEntity<ProjetoResponseDTO> submeter(@PathVariable Long id) {
		return ResponseEntity.ok(avaliacaoService.submeter(id));
	}

	@PostMapping("/avaliacao")
	@Operation(summary = "Registra a avaliacao de um projeto submetido",
			description = "So vale com o projeto em EM_ANALISE. O projeto passa para o status da decisao, e a avaliacao "
					+ "entra no historico. O parecer e obrigatorio para EM_CORRECAO e REPROVADO e opcional para APROVADO, "
					+ "com no maximo 2000 caracteres. Os espacos das pontas sao removidos antes de validar e gravar; "
					+ "um parecer so com espacos vira null.",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = AvaliacaoRequest.class),
							examples = {
									@ExampleObject(name = "Aprovar", value = AvaliacaoExemplos.REQUISICAO_APROVADO),
									@ExampleObject(name = "Pedir correcao", value = AvaliacaoExemplos.REQUISICAO_EM_CORRECAO),
									@ExampleObject(name = "Reprovar", value = AvaliacaoExemplos.REQUISICAO_REPROVADO)
							})))
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Avaliacao registrada",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ProjetoAvaliadoResponse.class),
							examples = @ExampleObject(name = "Pedido de correcao", value = AvaliacaoExemplos.RESPOSTA_AVALIACAO))),
			@ApiResponse(responseCode = "400", description = "Corpo invalido; o campo fields detalha cada violacao",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class),
							examples = {
									@ExampleObject(name = "Parecer obrigatorio", value = AvaliacaoExemplos.ERRO_400_PARECER),
									@ExampleObject(name = "Parecer acima do limite", value = AvaliacaoExemplos.ERRO_400_PARECER_LIMITE),
									@ExampleObject(name = "Decisao invalida", value = AvaliacaoExemplos.ERRO_400_DECISAO)
							})),
			@ApiResponse(responseCode = "404", description = "Projeto nao encontrado",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class),
							examples = @ExampleObject(name = "Projeto inexistente", value = AvaliacaoExemplos.ERRO_404_PROJETO))),
			@ApiResponse(responseCode = "409", description = "O projeto nao esta em EM_ANALISE",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class),
							examples = @ExampleObject(name = "Transicao invalida", value = AvaliacaoExemplos.ERRO_409_AVALIACAO)))
	})
	public ResponseEntity<ProjetoAvaliadoResponse> avaliar(@PathVariable Long id,
			@Valid @RequestBody AvaliacaoRequest requisicao) {
		return ResponseEntity.ok(avaliacaoService.avaliar(id, requisicao));
	}

	@GetMapping("/avaliacoes")
	@Operation(summary = "Consulta o historico de avaliacoes de um projeto",
			description = "Devolve as avaliacoes em ordem cronologica, ou uma lista vazia se nao houver nenhuma.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Historico de avaliacoes",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							array = @ArraySchema(schema = @Schema(implementation = AvaliacaoResponse.class)),
							examples = @ExampleObject(name = "Historico", value = AvaliacaoExemplos.RESPOSTA_HISTORICO))),
			@ApiResponse(responseCode = "404", description = "Projeto nao encontrado",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class),
							examples = @ExampleObject(name = "Projeto inexistente", value = AvaliacaoExemplos.ERRO_404_PROJETO)))
	})
	public ResponseEntity<List<AvaliacaoResponse>> listarAvaliacoes(@PathVariable Long id) {
		return ResponseEntity.ok(avaliacaoService.listarAvaliacoes(id));
	}

}
