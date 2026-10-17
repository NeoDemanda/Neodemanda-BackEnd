package com.neoenergia.neodemanda.motor.contexto;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import com.neoenergia.neodemanda.motor.entrada.DadosEntradaCalculo;
import com.neoenergia.neodemanda.motor.excecao.RegraNaoAplicavelException;
import com.neoenergia.neodemanda.motor.saida.MemoriaCalculo;

public class ContextoCalculo {

	private final DadosEntradaCalculo entrada;

	private final MemoriaCalculo memoria;

	private final Map<ChaveResultado, BigDecimal> resultados = new EnumMap<>(ChaveResultado.class);

	public ContextoCalculo(DadosEntradaCalculo entrada) {
		this.entrada = Objects.requireNonNull(entrada, "entrada é obrigatória");
		this.memoria = new MemoriaCalculo();
	}

	public DadosEntradaCalculo entrada() {
		return entrada;
	}

	public MemoriaCalculo memoria() {
		return memoria;
	}

	public void registrar(ChaveResultado chave, BigDecimal valor) {
		resultados.put(
				Objects.requireNonNull(chave, "chave é obrigatória"),
				Objects.requireNonNull(valor, "valor é obrigatório"));
	}

	public BigDecimal obter(ChaveResultado chave) {
		BigDecimal valor = resultados.get(chave);
		if (valor == null) {
			throw new RegraNaoAplicavelException("Nenhum valor registrado para a chave " + chave);
		}
		return valor;
	}

}
