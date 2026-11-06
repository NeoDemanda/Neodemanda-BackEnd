package com.neoenergia.neodemanda.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.neoenergia.neodemanda.model.CalculoGuardado;

@Repository
public class CalculoRepository {

	private final Map<Long, CalculoGuardado> calculosEmMemoria = new ConcurrentHashMap<>();

	public void salvar(CalculoGuardado calculo) {
		calculosEmMemoria.put(calculo.projetoId(), calculo);
	}

	public Optional<CalculoGuardado> buscarPorProjeto(Long projetoId) {
		return Optional.ofNullable(calculosEmMemoria.get(projetoId));
	}

}
