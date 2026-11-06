package com.neoenergia.neodemanda.motor.regra;

import java.math.BigDecimal;

import com.neoenergia.neodemanda.model.enums.TipoEdificacao;
import com.neoenergia.neodemanda.motor.contexto.ChaveResultado;
import com.neoenergia.neodemanda.motor.contexto.ContextoCalculo;
import com.neoenergia.neodemanda.motor.norma.ParametroCalculo;
import com.neoenergia.neodemanda.motor.saida.Enquadramento;
import com.neoenergia.neodemanda.motor.saida.UnidadeMedida;

final class RegraEnquadramento extends RegraDemanda {

	RegraEnquadramento() {
		super("ENQUADRAMENTO", "Enquadramento do atendimento", "Enquadramento",
				ParametroCalculo.LIMITE_BT_RESIDENCIAL_KVA.referencia());
	}

	@Override
	public void aplicar(ContextoCalculo contexto) {
		BigDecimal ded = contexto.obter(ChaveResultado.DEMANDA_TOTAL);
		TipoEdificacao tipo = contexto.entrada().tipoEdificacao();
		ParametroCalculo parametroLimite = limiteDo(tipo);
		BigDecimal limite = parametroLimite.valor();
		String textoDed = FormatoMemorial.calculado(ded);
		String textoLimite = FormatoMemorial.numero(limite);
		boolean grupoA = contexto.entrada().clienteGrupoA();

		Enquadramento enquadramento;
		String aplicada;
		if (grupoA) {
			enquadramento = Enquadramento.MEDIA_TENSAO;
			aplicada = "Cliente do Grupo A presente → média tensão";
		}
		else if (ded.compareTo(limite) <= 0) {
			enquadramento = Enquadramento.BAIXA_TENSAO;
			aplicada = textoDed + " ≤ " + textoLimite + " → baixa tensão";
		}
		else {
			enquadramento = Enquadramento.MEDIA_TENSAO;
			aplicada = textoDed + " > " + textoLimite + " → média tensão";
		}

		contexto.registrarEnquadramento(enquadramento);
		contexto.memoria().adicionar(passo(contexto,
				"Cliente do Grupo A → média tensão; Ded ≤ " + textoLimite + " → baixa tensão; Ded > " + textoLimite
						+ " → média tensão",
				aplicada, ded, UnidadeMedida.KVA)
				.condicao("Edificação " + descricaoDo(tipo, contexto.entrada().condominioSmartStudio()) + ", "
						+ (grupoA ? "com" : "sem") + " cliente do Grupo A")
				.entrada("Ded", ded, UnidadeMedida.KVA)
				.entrada("Limite de baixa tensão", limite, UnidadeMedida.KVA)
				.resultadoTexto(enquadramento.descricao())
				.referenciaNormativa(parametroLimite.referencia())
				.build());
	}

	private static ParametroCalculo limiteDo(TipoEdificacao tipo) {
		return switch (tipo) {
			case RESIDENCIAL -> ParametroCalculo.LIMITE_BT_RESIDENCIAL_KVA;
			case COMERCIAL, MISTO -> ParametroCalculo.LIMITE_BT_COMERCIAL_MISTO_KVA;
			default -> throw new IllegalStateException("Tipo sem limite de enquadramento: " + tipo);
		};
	}

	private static String descricaoDo(TipoEdificacao tipo, boolean smartStudio) {
		return switch (tipo) {
			case RESIDENCIAL -> smartStudio ? "residencial Smart/Studio" : "residencial";
			case COMERCIAL -> "comercial";
			case MISTO -> "mista";
			default -> throw new IllegalStateException("Tipo sem limite de enquadramento: " + tipo);
		};
	}

}
