package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;
import java.math.MathContext;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.norma.ParametroCalculo;
import com.neoenergia.neodemanda.motor.saida.PassoCalculo;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraParcelaA extends RegraDemanda {

	private static final BigDecimal WATTS_POR_KW = new BigDecimal("1000");

	private static final MathContext PRECISAO = MathContext.DECIMAL128;

	private static final String FORMULA = "a = ( (Pilum / 1000) / FP × Fdi ) + ( (Ptug / 1000) / FP × Fdt )";

	RegraParcelaA() {
		super("A", "Parcela de iluminação e tomadas de serviços", "a", ParametroCalculo.FATOR_POTENCIA.referencia());
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		BigDecimal pilum = contexto.entrada().iluminacaoAreasComunsW();
		BigDecimal ptug = contexto.entrada().tomadasAreasComunsW();
		BigDecimal fp = ParametroCalculo.FATOR_POTENCIA.valor();
		BigDecimal fdi = ParametroCalculo.FATOR_DEMANDA_ILUMINACAO.valor();
		BigDecimal fdt = ParametroCalculo.FATOR_DEMANDA_TOMADAS.valor();

		BigDecimal parcelaIluminacao = pilum.divide(WATTS_POR_KW, PRECISAO).divide(fp, PRECISAO).multiply(fdi);
		BigDecimal parcelaTomadas = ptug.divide(WATTS_POR_KW, PRECISAO).divide(fp, PRECISAO).multiply(fdt);
		BigDecimal a = parcelaIluminacao.add(parcelaTomadas);

		String aplicada = "a = ( (" + FormatoMemorial.numero(pilum) + " / 1000) / " + FormatoMemorial.numero(fp) + " × "
				+ FormatoMemorial.numero(fdi) + " ) + ( (" + FormatoMemorial.numero(ptug) + " / 1000) / "
				+ FormatoMemorial.numero(fp) + " × " + FormatoMemorial.numero(fdt) + " )";

		contexto.registrar(ChaveResultado.A, a);
		PassoCalculo.Builder passo = passo(contexto, FORMULA, aplicada, a, UnidadeMedida.KVA)
				.entrada("Pilum", pilum, UnidadeMedida.W)
				.entrada("Ptug", ptug, UnidadeMedida.W)
				.entrada("FP", fp, UnidadeMedida.ADIMENSIONAL)
				.entrada("Fdi", fdi, UnidadeMedida.ADIMENSIONAL)
				.entrada("Fdt", fdt, UnidadeMedida.ADIMENSIONAL);
		citar(passo, ParametroCalculo.FATOR_POTENCIA, ParametroCalculo.FATOR_DEMANDA_ILUMINACAO,
				ParametroCalculo.FATOR_DEMANDA_TOMADAS);
		contexto.memoria().adicionar(passo.build());
	}

}
