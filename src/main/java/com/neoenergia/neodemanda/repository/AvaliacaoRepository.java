package com.neoenergia.neodemanda.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;

import com.neoenergia.neodemanda.model.Avaliacao;

@Repository
public class AvaliacaoRepository {

	private final Map<Long, List<Avaliacao>> avaliacoesEmMemoria = new ConcurrentHashMap<>();

	public void salvar(Avaliacao avaliacao) {
		avaliacoesEmMemoria.computeIfAbsent(avaliacao.projetoId(), id -> new CopyOnWriteArrayList<>()).add(avaliacao);
	}

	public List<Avaliacao> listarPorProjeto(Long projetoId) {
		return List.copyOf(avaliacoesEmMemoria.getOrDefault(projetoId, List.of()));
	}

}
