package com.neoenergia.neodemanda.motor.saida;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoriaCalculo {

	private final List<PassoCalculo> passos = new ArrayList<>();

	public void adicionar(PassoCalculo passo) {
		Objects.requireNonNull(passo, "passo é obrigatório");
		if (passo.ordem() != proximaOrdem()) {
			throw new IllegalArgumentException(
					"ordem " + passo.ordem() + " fora de sequência; esperada " + proximaOrdem());
		}
		passos.add(passo);
	}

	public List<PassoCalculo> passos() {
		return List.copyOf(passos);
	}

	public int proximaOrdem() {
		return passos.size() + 1;
	}

	public List<NormaRevisao> normasUsadas() {
		return passos.stream()
				.flatMap(passo -> passo.referenciasNormativas().stream())
				.map(ReferenciaNormativa::normaRevisao)
				.distinct()
				.toList();
	}

}
