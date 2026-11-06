package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.entrada.EstacaoRecargaEntrada;
import com.neoenergia.neodemanda.motor.entrada.TipoConexaoRecarga;
import com.neoenergia.neodemanda.motor.excecao.CoberturaNormativaException;
import com.neoenergia.neodemanda.motor.norma.Quadro33FatorDemandaVe;
import com.neoenergia.neodemanda.motor.saida.PassoCalculo;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraDve extends RegraDemanda {

	private static final String FORMULA = "Dve = ∑ Pve × Fd_VE";

	RegraDve() {
		super("DVE", "Demanda de recarga de veículos elétricos", "Dve", Quadro33FatorDemandaVe.referencia());
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		List<EstacaoRecargaEntrada> estacoes = contexto.entrada().estacoesRecarga();
		if (estacoes.isEmpty()) {
			contexto.registrar(ChaveResultado.DVE, BigDecimal.ZERO);
			contexto.memoria().adicionar(passo(contexto, FORMULA, "Dve = 0", BigDecimal.ZERO, UnidadeMedida.KVA)
					.condicao("Sem estações de recarga")
					.entrada("Quantidade de estações", BigDecimal.ZERO, UnidadeMedida.ADIMENSIONAL)
					.referenciaNormativa(Quadro33FatorDemandaVe.referencia())
					.build());
			return;
		}

		boolean medidorIndividual = estacoes.stream()
				.anyMatch(estacao -> estacao.tipoConexao() == TipoConexaoRecarga.MEDIDOR_INDIVIDUAL_APARTAMENTO);
		if (medidorIndividual) {
			throw new CoberturaNormativaException("A documentação não define a regra de cálculo para estação de recarga "
					+ "conectada no medidor individual do apartamento.");
		}

		int totalEstacoes = contexto.entrada().totalEstacoesRecarga();
		Quadro33FatorDemandaVe linha = Quadro33FatorDemandaVe.porEstacoes(totalEstacoes);
		BigDecimal somaPve = BigDecimal.ZERO;
		List<String> termos = new ArrayList<>();
		for (EstacaoRecargaEntrada estacao : estacoes) {
			somaPve = somaPve.add(estacao.potenciaKw().multiply(quantidade(estacao.quantidade())));
			termos.add(FormatoMemorial.numero(estacao.potenciaKw()) + " × " + estacao.quantidade());
		}
		BigDecimal dve = somaPve.multiply(linha.fator());

		contexto.registrar(ChaveResultado.DVE, dve);
		PassoCalculo.Builder passo = passo(contexto, FORMULA,
				"Dve = (" + String.join(" + ", termos) + ") × " + FormatoMemorial.numero(linha.fator()), dve,
				UnidadeMedida.KVA)
				.condicao(totalEstacoes + " estações → Quadro 33, faixa " + linha.faixa() + ": "
						+ FormatoMemorial.numero(linha.fator()));
		for (int i = 0; i < estacoes.size(); i++) {
			int j = i + 1;
			passo.entrada("Pve" + j, estacoes.get(i).potenciaKw(), UnidadeMedida.KW)
					.entrada("Quantidade da estação " + j, quantidade(estacoes.get(i).quantidade()), UnidadeMedida.ADIMENSIONAL);
		}
		passo.entrada("Fd_VE", linha.fator(), UnidadeMedida.ADIMENSIONAL)
				.referenciaNormativa(Quadro33FatorDemandaVe.referencia());
		contexto.memoria().adicionar(passo.build());
	}

}
