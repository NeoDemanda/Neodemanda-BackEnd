package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.norma.ParametroCalculo;
import com.neoenergia.neodemanda.motor.norma.Tabela2PadraoEntradaBt;
import com.neoenergia.neodemanda.motor.saida.Enquadramento;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraPadraoEntrada extends RegraDemanda {

	private static final String FORMULA = "Padrão de entrada = Tabela 2(Dproj)";

	RegraPadraoEntrada() {
		super("PADRAO_ENTRADA", "Padrão de entrada", "Padrão de entrada", Tabela2PadraoEntradaBt.referencia());
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		if (contexto.enquadramento() == Enquadramento.MEDIA_TENSAO) {
			registrarForaDoEscopo(contexto);
			return;
		}

		BigDecimal dproj = contexto.obter(ChaveResultado.DPROJ);
		Tabela2PadraoEntradaBt linha = Tabela2PadraoEntradaBt.porDemandaProjeto(dproj);
		String textoDproj = FormatoMemorial.calculado(dproj);
		contexto.registrarPadraoEntrada(linha);
		contexto.memoria().adicionar(passo(contexto, FORMULA, "Padrão de entrada = Tabela 2(" + textoDproj + ")", dproj,
				UnidadeMedida.KVA)
				.condicao("Dproj = " + textoDproj + " kVA → Tabela 2, faixa " + linha.faixa())
				.entrada("Dproj", dproj, UnidadeMedida.KVA)
				.resultadoTexto("Condutor " + linha.condutorPorFase() + " por fase; disjuntor geral "
						+ FormatoMemorial.numero(linha.disjuntorA()) + " A; carcaça "
						+ FormatoMemorial.numero(linha.carcacaA()) + " A; eletroduto " + linha.eletroduto())
				.referenciaNormativa(Tabela2PadraoEntradaBt.referencia())
				.build());
	}

	private void registrarForaDoEscopo(ContextoCalculo contexto) {
		BigDecimal ded = contexto.obter(ChaveResultado.DEMANDA_TOTAL);
		contexto.memoria().adicionar(passo(contexto, "Dimensionamento em média tensão não definido na documentação",
				"Não dimensionado", ded, UnidadeMedida.KVA)
				.condicao("Atendimento em média tensão")
				.entrada("Ded", ded, UnidadeMedida.KVA)
				.resultadoTexto("Dimensionamento em média tensão fora do escopo")
				.referenciaNormativa(ParametroCalculo.LIMITE_BT_RESIDENCIAL_KVA.referencia())
				.build());
	}

}
