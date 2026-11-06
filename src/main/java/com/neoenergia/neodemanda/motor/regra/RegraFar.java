package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.excecao.CoberturaNormativaException;
import com.neoenergia.neodemanda.motor.norma.ReferenciaTermo;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraFar extends RegraDemanda {

	RegraFar() {
		super("FAR", "Demanda de ar-condicionado comercial", "far", ReferenciaTermo.FAR.referencia());
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		if (!contexto.entrada().condicionadoresAr().isEmpty()) {
			throw new CoberturaNormativaException(ReferenciaTermo.TABELA_11.identificacao()
					+ " não está disponível na documentação: não é possível calcular a demanda dos condicionadores de ar informados.");
		}
		contexto.registrar(ChaveResultado.FAR, BigDecimal.ZERO);
		contexto.memoria().adicionar(passo(contexto, "far = ∑ PAC × Fd_AC", "far = 0", BigDecimal.ZERO, UnidadeMedida.KVA)
				.condicao("Sem condicionadores de ar")
				.entrada("Quantidade de condicionadores", BigDecimal.ZERO, UnidadeMedida.ADIMENSIONAL)
				.referenciaNormativa(ReferenciaTermo.FAR.referencia())
				.build());
	}

}
