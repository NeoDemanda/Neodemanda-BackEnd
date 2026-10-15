package com.neoenergia.neodemanda.motor.regra;

import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;

public interface RegraCalculo {

	String id();

	String referenciaNormativa();

	boolean aplicavel(ContextoCalculo contexto);

	void aplicar(ContextoCalculo contexto);

}
