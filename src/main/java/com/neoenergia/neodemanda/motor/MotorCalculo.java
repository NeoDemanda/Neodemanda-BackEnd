package com.neoenergia.neodemanda.motor;

import java.util.List;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.entrada.DadosEntradaCalculo;
import com.neoenergia.neodemanda.motor.excecao.EntradaInvalidaException;
import com.neoenergia.neodemanda.motor.regra.RegraCalculo;
import com.neoenergia.neodemanda.motor.saida.ResultadoCalculo;

public class MotorCalculo {

	private final List<RegraCalculo> regras;

	public MotorCalculo(List<RegraCalculo> regras) {
		this.regras = List.copyOf(regras);
	}

	public ResultadoCalculo calcular(DadosEntradaCalculo entrada) {
		if (entrada == null) {
			throw new EntradaInvalidaException("A entrada do cálculo não pode ser nula");
		}
		ContextoCalculo contexto = new ContextoCalculo(entrada);
		for (RegraCalculo regra : regras) {
			if (regra.aplicavel(contexto)) {
				regra.aplicar(contexto);
			}
		}
		return new ResultadoCalculo(contexto.obter(ChaveResultado.DEMANDA_TOTAL), contexto.memoria());
	}

}
