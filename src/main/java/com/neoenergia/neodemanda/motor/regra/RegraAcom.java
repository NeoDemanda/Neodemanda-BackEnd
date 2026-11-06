package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;
import java.math.MathContext;

import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.norma.ParametroCalculo;
import com.neoenergia.neodemanda.motor.saida.PassoCalculo;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraAcom extends RegraDemanda {

	private static final MathContext PRECISAO = MathContext.DECIMAL128;

	RegraAcom() {
		super("ACOM", "Demanda de iluminação e tomadas comerciais", "acom",
				ParametroCalculo.DEGRAU_COMERCIAL_KW.referencia());
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		BigDecimal pcom = contexto.entrada().cargaComercialKw();
		BigDecimal fp = ParametroCalculo.FATOR_POTENCIA.valor();
		BigDecimal degrau = ParametroCalculo.DEGRAU_COMERCIAL_KW.valor();
		BigDecimal fatorAteDegrau = ParametroCalculo.FATOR_COMERCIAL_ATE_DEGRAU.valor();
		BigDecimal fatorExcedente = ParametroCalculo.FATOR_COMERCIAL_EXCEDENTE.valor();
		String textoDegrau = FormatoMemorial.numero(degrau);
		String textoFatorAte = FormatoMemorial.numero(fatorAteDegrau);
		String textoFatorExcedente = FormatoMemorial.numero(fatorExcedente);
		String formula = "acom = ( (min(" + textoDegrau + ", Pcom) / FP) × " + textoFatorAte + " ) + ( (max(0, Pcom - "
				+ textoDegrau + ") / FP) × " + textoFatorExcedente + " )";

		BigDecimal acom;
		String aplicada;
		String condicao = null;
		if (pcom.signum() == 0) {
			acom = BigDecimal.ZERO;
			aplicada = "acom = 0";
			condicao = "Sem carga comercial";
		}
		else {
			BigDecimal ateDegrau = pcom.min(degrau);
			BigDecimal excedente = pcom.subtract(degrau).max(BigDecimal.ZERO);
			acom = ateDegrau.divide(fp, PRECISAO).multiply(fatorAteDegrau)
					.add(excedente.divide(fp, PRECISAO).multiply(fatorExcedente));
			String textoPcom = FormatoMemorial.numero(pcom);
			String textoFp = FormatoMemorial.numero(fp);
			aplicada = "acom = ( (min(" + textoDegrau + "; " + textoPcom + ") / " + textoFp + ") × " + textoFatorAte
					+ " ) + ( (max(0; " + textoPcom + " - " + textoDegrau + ") / " + textoFp + ") × "
					+ textoFatorExcedente + " )";
		}

		contexto.registrar(ChaveResultado.ACOM, acom);
		PassoCalculo.Builder passo = passo(contexto, formula, aplicada, acom, UnidadeMedida.KVA)
				.condicao(condicao)
				.entrada("Pcom", pcom, UnidadeMedida.KW)
				.entrada("FP", fp, UnidadeMedida.ADIMENSIONAL)
				.entrada("Degrau comercial", degrau, UnidadeMedida.KW)
				.entrada("Fator até o degrau", fatorAteDegrau, UnidadeMedida.ADIMENSIONAL)
				.entrada("Fator do excedente", fatorExcedente, UnidadeMedida.ADIMENSIONAL);
		citar(passo, ParametroCalculo.DEGRAU_COMERCIAL_KW, ParametroCalculo.FATOR_COMERCIAL_ATE_DEGRAU,
				ParametroCalculo.FATOR_COMERCIAL_EXCEDENTE);
		contexto.memoria().adicionar(passo.build());
	}

}
