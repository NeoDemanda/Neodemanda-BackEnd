package com.neoenergia.neodemanda.dto;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.model.enums.TipoEdificacao;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Dados de entrada do cadastro de projeto, vindos do formulario de Novo Projeto.
 *
 * <p>Os limites de tamanho espelham as colunas de
 * {@link com.neoenergia.neodemanda.model.Projeto}. Ficam de fora os campos que
 * o cliente nao declara: {@code id} e as datas (gerados na persistencia),
 * {@code status} (atribuido pelo servidor) e {@code demandaCalculadaKva}, que e
 * resultado do calculo normativo e nao entrada do usuario.
 */
public record ProjetoRequestDTO(

		@NotBlank(message = "O nome do projeto e obrigatorio")
		@Size(max = 150, message = "O nome do projeto deve ter no maximo 150 caracteres")
		String nomeProjeto,

		@NotBlank(message = "O responsavel tecnico e obrigatorio")
		@Size(max = 150, message = "O responsavel tecnico deve ter no maximo 150 caracteres")
		String responsavelTecnico,

		@NotBlank(message = "O CREA do responsavel e obrigatorio")
		@Size(max = 30, message = "O CREA do responsavel deve ter no maximo 30 caracteres")
		String creaResponsavel,

		@NotNull(message = "A quantidade de unidades consumidoras e obrigatoria")
		@Positive(message = "A quantidade de unidades consumidoras deve ser maior que zero")
		Integer quantidadeUnidadesConsumidoras,

		@NotNull(message = "A carga instalada e obrigatoria")
		@DecimalMin(value = "0.001", message = "A carga instalada deve ser de no minimo 0.001 kVA")
		BigDecimal cargaInstaladaKva,

		@NotNull(message = "O tipo de edificacao e obrigatorio")
		TipoEdificacao tipoEdificacao) {

}
