package com.neoenergia.neodemanda.motor;

import java.util.List;

import com.neoenergia.neodemanda.motor.entrada.DadosEntradaCalculo;
import com.neoenergia.neodemanda.motor.entrada.ValidadorEntradaCalculo;
import com.neoenergia.neodemanda.motor.regra.RegraCalculo;
import com.neoenergia.neodemanda.motor.regra.RegrasDemanda;
import com.neoenergia.neodemanda.motor.saida.ResultadoCalculo;

public class CalculadoraDemanda {

	private final MotorCalculo motor;

	public CalculadoraDemanda() {
		this(RegrasDemanda.todas());
	}

	CalculadoraDemanda(List<RegraCalculo> regras) {
		this.motor = new MotorCalculo(regras);
	}

	public ResultadoCalculo calcular(DadosEntradaCalculo entrada) {
		ValidadorEntradaCalculo.validar(entrada);
		return motor.calcular(entrada);
	}

}
