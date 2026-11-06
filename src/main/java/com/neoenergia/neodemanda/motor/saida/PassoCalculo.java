package com.neoenergia.neodemanda.motor.saida;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record PassoCalculo(
		int ordem,
		String idRegra,
		String descricao,
		String simbolo,
		String formulaSimbolica,
		String formulaAplicada,
		String condicao,
		List<EntradaPasso> entradas,
		BigDecimal valorProduzido,
		UnidadeMedida unidade,
		String resultadoTexto,
		List<ReferenciaNormativa> referenciasNormativas) {

	public PassoCalculo {
		if (ordem < 1) {
			throw new IllegalArgumentException("ordem é obrigatória e deve ser maior que zero");
		}
		exigirTexto(idRegra, "idRegra");
		exigirTexto(descricao, "descricao");
		exigirTexto(simbolo, "simbolo");
		exigirTexto(formulaSimbolica, "formulaSimbolica");
		exigirTexto(formulaAplicada, "formulaAplicada");
		entradas = exigirLista(entradas, "entradas");
		Objects.requireNonNull(valorProduzido, "valorProduzido é obrigatório");
		Objects.requireNonNull(unidade, "unidade é obrigatória");
		referenciasNormativas = exigirLista(referenciasNormativas, "referenciasNormativas");
	}

	public static Builder builder() {
		return new Builder();
	}

	static void exigirTexto(String valor, String campo) {
		if (valor == null || valor.isBlank()) {
			throw new IllegalArgumentException(campo + " é obrigatório");
		}
	}

	private static <T> List<T> exigirLista(List<T> valores, String campo) {
		if (valores == null || valores.isEmpty()) {
			throw new IllegalArgumentException(campo + " é obrigatório");
		}
		return List.copyOf(valores);
	}

	public static final class Builder {

		private int ordem;

		private String idRegra;

		private String descricao;

		private String simbolo;

		private String formulaSimbolica;

		private String formulaAplicada;

		private String condicao;

		private final List<EntradaPasso> entradas = new ArrayList<>();

		private BigDecimal valorProduzido;

		private UnidadeMedida unidade;

		private String resultadoTexto;

		private final List<ReferenciaNormativa> referenciasNormativas = new ArrayList<>();

		private Builder() {
		}

		public Builder ordem(int ordem) {
			this.ordem = ordem;
			return this;
		}

		public Builder idRegra(String idRegra) {
			this.idRegra = idRegra;
			return this;
		}

		public Builder descricao(String descricao) {
			this.descricao = descricao;
			return this;
		}

		public Builder simbolo(String simbolo) {
			this.simbolo = simbolo;
			return this;
		}

		public Builder formulaSimbolica(String formulaSimbolica) {
			this.formulaSimbolica = formulaSimbolica;
			return this;
		}

		public Builder formulaAplicada(String formulaAplicada) {
			this.formulaAplicada = formulaAplicada;
			return this;
		}

		public Builder condicao(String condicao) {
			this.condicao = condicao;
			return this;
		}

		public Builder entrada(String nome, BigDecimal valor, UnidadeMedida unidade) {
			this.entradas.add(new EntradaPasso(nome, valor, unidade));
			return this;
		}

		public Builder valorProduzido(BigDecimal valorProduzido) {
			this.valorProduzido = valorProduzido;
			return this;
		}

		public Builder unidade(UnidadeMedida unidade) {
			this.unidade = unidade;
			return this;
		}

		public Builder resultadoTexto(String resultadoTexto) {
			this.resultadoTexto = resultadoTexto;
			return this;
		}

		public Builder referenciaNormativa(ReferenciaNormativa referencia) {
			this.referenciasNormativas.add(Objects.requireNonNull(referencia, "referenciaNormativa é obrigatória"));
			return this;
		}

		public PassoCalculo build() {
			return new PassoCalculo(ordem, idRegra, descricao, simbolo, formulaSimbolica, formulaAplicada, condicao,
					entradas, valorProduzido, unidade, resultadoTexto, referenciasNormativas);
		}

	}

}
