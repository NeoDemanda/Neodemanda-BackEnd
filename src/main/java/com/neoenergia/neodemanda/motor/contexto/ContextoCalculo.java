package com.neoenergia.neodemanda.motor.contexto;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import com.neoenergia.neodemanda.motor.entrada.DadosEntradaCalculo;
import com.neoenergia.neodemanda.motor.excecao.RegraNaoAplicavelException;
import com.neoenergia.neodemanda.motor.norma.Tabela2PadraoEntradaBt;
import com.neoenergia.neodemanda.motor.saida.Enquadramento;
import com.neoenergia.neodemanda.motor.saida.MemoriaCalculo;

public class ContextoCalculo {

	private final DadosEntradaCalculo entrada;

	private final MemoriaCalculo memoria;

	private final Map<ChaveResultado, BigDecimal> resultados = new EnumMap<>(ChaveResultado.class);

	private Enquadramento enquadramento;

	private Tabela2PadraoEntradaBt padraoEntrada;

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

	public BigDecimal consultar(ChaveResultado chave) {
		return resultados.get(chave);
	}

	public void registrarEnquadramento(Enquadramento enquadramento) {
		this.enquadramento = Objects.requireNonNull(enquadramento, "enquadramento é obrigatório");
	}

	public Enquadramento enquadramento() {
		return enquadramento;
	}

	public void registrarPadraoEntrada(Tabela2PadraoEntradaBt padraoEntrada) {
		this.padraoEntrada = Objects.requireNonNull(padraoEntrada, "padraoEntrada é obrigatório");
	}

	public Tabela2PadraoEntradaBt padraoEntrada() {
		return padraoEntrada;
	}

}
