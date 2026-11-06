package com.neoenergia.neodemanda.model;

import java.time.OffsetDateTime;

import com.neoenergia.neodemanda.motor.entrada.DadosEntradaCalculo;
import com.neoenergia.neodemanda.motor.saida.ResultadoCalculo;

public record CalculoGuardado(
		Long projetoId,
		long revisaoProjeto,
		OffsetDateTime calculadoEm,
		DadosEntradaCalculo entradas,
		ResultadoCalculo resultado) {

}
