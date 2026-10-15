package com.neoenergia.neodemanda.motor.saida;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoriaCalculo {

	private final List<PassoCalculo> passos = new ArrayList<>();

	public void adicionar(PassoCalculo passo) {
		passos.add(Objects.requireNonNull(passo, "passo é obrigatório"));
	}

	public List<PassoCalculo> passos() {
		return List.copyOf(passos);
	}

}
