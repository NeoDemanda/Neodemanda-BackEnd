package com.neoenergia.neodemanda.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neoenergia.neodemanda.dto.CalculoRequest;
import com.neoenergia.neodemanda.dto.CalculoResponse;
import com.neoenergia.neodemanda.exception.ApiError;
import com.neoenergia.neodemanda.service.CalculoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/projetos/{id}/calculo")
@Tag(name = "Calculo de demanda", description = "Calculo da demanda de um projeto e consulta do memorial")
public class CalculoController {

	private final CalculoService calculoService;

	public CalculoController(CalculoService calculoService) {
		this.calculoService = calculoService;
	}

	@PostMapping
	@Operation(summary = "Calcula a demanda de um projeto",
			description = "Calcula a demanda de um projeto em RASCUNHO ou EM_CORRECAO, guarda o resultado e as entradas usadas e "
					+ "devolve o resultado com o memorial. Tipo de edificacao, unidades consumidoras e carga instalada "
					+ "vem do projeto; se enviados no corpo, sao ignorados. Recalcular substitui o calculo anterior; "
					+ "um calculo que falha mantem o anterior. O bloco entradas e as entradas do memorial em m2, W, kW e cv "
					+ "devolvem os valores sem arredondar. Nos demais valores do resultado e do memorial, kVA e A tem "
					+ "2 casas, fatores tem 4 casas e contagens sao inteiras (HALF_UP).",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = CalculoRequest.class),
							examples = @ExampleObject(name = "Exemplo 1", value = CalculoExemplos.REQUISICAO_EXEMPLO_1))))
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Demanda calculada",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = CalculoResponse.class),
							examples = @ExampleObject(name = "Exemplo 1", value = CalculoExemplos.RESPOSTA_EXEMPLO_1))),
			@ApiResponse(responseCode = "400",
					description = "Corpo invalido (fields detalha cada campo) ou entrada recusada pelo dominio (fields vazio)",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class),
							examples = {
									@ExampleObject(name = "Campo invalido", value = CalculoExemplos.ERRO_400_CAMPO),
									@ExampleObject(name = "Limite de digitos", value = CalculoExemplos.ERRO_400_LIMITE),
									@ExampleObject(name = "Valor de tipo invalido", value = CalculoExemplos.ERRO_400_TIPO),
									@ExampleObject(name = "Corpo mal formatado", value = CalculoExemplos.ERRO_400_CORPO),
									@ExampleObject(name = "Coerencia entre tipo e cargas", value = CalculoExemplos.ERRO_400_DOMINIO),
									@ExampleObject(name = "Carga instalada do projeto", value = CalculoExemplos.ERRO_400_CARGA_PROJETO)
							})),
			@ApiResponse(responseCode = "404", description = "Projeto nao encontrado",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class),
							examples = @ExampleObject(name = "Projeto inexistente", value = CalculoExemplos.ERRO_404_PROJETO))),
			@ApiResponse(responseCode = "409", description = "O status do projeto nao permite calcular: so RASCUNHO e EM_CORRECAO sao aceitos",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class),
							examples = {
									@ExampleObject(name = "Projeto submetido", value = CalculoExemplos.ERRO_409_SUBMETIDO),
									@ExampleObject(name = "Projeto arquivado", value = CalculoExemplos.ERRO_409_ARQUIVADO)
							})),
			@ApiResponse(responseCode = "422",
					description = "A documentacao normativa nao cobre o valor informado ou nao define a regra",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class),
							examples = {
									@ExampleObject(name = "Quadro 36", value = CalculoExemplos.ERRO_422_QUADRO_36),
									@ExampleObject(name = "Tabela 11", value = CalculoExemplos.ERRO_422_TABELA_11),
									@ExampleObject(name = "Industrial", value = CalculoExemplos.ERRO_422_INDUSTRIAL)
							}))
	})
	public ResponseEntity<CalculoResponse> calcular(@PathVariable Long id, @Valid @RequestBody CalculoRequest requisicao) {
		return ResponseEntity.ok(calculoService.calcular(id, requisicao));
	}

	@GetMapping
	@Operation(summary = "Consulta o ultimo calculo de um projeto",
			description = "Devolve o ultimo calculo guardado, em qualquer status do projeto. Editar o projeto invalida "
					+ "o calculo: o sinal e o 404 nesta rota.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Calculo encontrado",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = CalculoResponse.class),
							examples = @ExampleObject(name = "Exemplo 1", value = CalculoExemplos.RESPOSTA_EXEMPLO_1))),
			@ApiResponse(responseCode = "404", description = "Projeto inexistente, nunca calculado ou calculo invalidado",
					content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class),
							examples = {
									@ExampleObject(name = "Sem calculo valido", value = CalculoExemplos.ERRO_404_CALCULO),
									@ExampleObject(name = "Projeto inexistente", value = CalculoExemplos.ERRO_404_PROJETO)
							}))
	})
	public ResponseEntity<CalculoResponse> consultar(@PathVariable Long id) {
		return ResponseEntity.ok(calculoService.consultar(id));
	}

}
