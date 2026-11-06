package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;
import java.util.Arrays;

import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.norma.ParametroCalculo;
import com.neoenergia.neodemanda.motor.saida.PassoCalculo;
import com.neoenergia.neodemanda.motor.saida.ReferenciaNormativa;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

abstract class RegraDemanda implements RegraCalculo {

	static final String SEM_APARTAMENTOS = "Sem apartamentos: Fc e Fr não se aplicam";

	private final String id;

	private final String descricao;

	private final String simbolo;

	private final ReferenciaNormativa referenciaPrincipal;

	RegraDemanda(String id, String descricao, String simbolo, ReferenciaNormativa referenciaPrincipal) {
		this.id = id;
		this.descricao = descricao;
		this.simbolo = simbolo;
		this.referenciaPrincipal = referenciaPrincipal;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public String referenciaNormativa() {
		String norma = referenciaPrincipal.norma() + " " + referenciaPrincipal.revisao();
		return referenciaPrincipal.item() == null ? norma : norma + ", " + referenciaPrincipal.item();
	}

	@Override
	public boolean aplicavel(ContextoCalculo contexto) {
		return true;
	}

	final PassoCalculo.Builder passo(ContextoCalculo contexto, String formulaSimbolica, String formulaAplicada,
			BigDecimal valor, UnidadeMedida unidade) {
		return PassoCalculo.builder()
				.ordem(contexto.memoria().proximaOrdem())
				.idRegra(id)
				.descricao(descricao)
				.simbolo(simbolo)
				.formulaSimbolica(formulaSimbolica)
				.formulaAplicada(formulaAplicada)
				.valorProduzido(valor)
				.unidade(unidade);
	}

	static void citar(PassoCalculo.Builder passo, ParametroCalculo... parametros) {
		Arrays.stream(parametros)
				.map(ParametroCalculo::referencia)
				.distinct()
				.forEach(passo::referenciaNormativa);
	}

	static BigDecimal quantidade(int valor) {
		return BigDecimal.valueOf(valor);
	}

}
