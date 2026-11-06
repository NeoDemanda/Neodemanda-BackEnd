package com.neoenergia.neodemanda.motor.entrada;

import java.util.List;

import com.neoenergia.neodemanda.model.enums.TipoEdificacao;
import com.neoenergia.neodemanda.motor.excecao.CoberturaNormativaException;
import com.neoenergia.neodemanda.motor.excecao.EntradaInvalidaException;
import com.neoenergia.neodemanda.motor.norma.ParametroCalculo;
import com.neoenergia.neodemanda.motor.saida.ReferenciaNormativa;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public final class ValidadorEntradaCalculo {

	private static final Validator VALIDADOR = Validation.buildDefaultValidatorFactory().getValidator();

	private static final String ITEM_DE_LISTA = ".<list element>";

	private ValidadorEntradaCalculo() {
	}

	public static void validar(DadosEntradaCalculo entrada) {
		if (entrada == null) {
			throw new EntradaInvalidaException("A entrada do cálculo não pode ser nula");
		}
		validarCampos(entrada);
		validarCobertura(entrada);
		validarCoerenciaEntreTipoECargas(entrada);
	}

	private static void validarCampos(DadosEntradaCalculo entrada) {
		List<String> erros = VALIDADOR.validate(entrada).stream()
				.map(ValidadorEntradaCalculo::mensagem)
				.sorted()
				.toList();
		if (!erros.isEmpty()) {
			throw new EntradaInvalidaException(String.join("; ", erros));
		}
	}

	private static String mensagem(ConstraintViolation<DadosEntradaCalculo> violacao) {
		String campo = violacao.getPropertyPath().toString().replace(ITEM_DE_LISTA, "");
		return campo + ": " + violacao.getMessage();
	}

	private static void validarCobertura(DadosEntradaCalculo entrada) {
		TipoEdificacao tipo = entrada.tipoEdificacao();
		if (tipo == TipoEdificacao.INDUSTRIAL) {
			throw new CoberturaNormativaException(
					"A documentação (" + normaDoEnquadramento() + ") não cobre edificações industriais.");
		}
		if (entrada.condominioSmartStudio() && tipo != TipoEdificacao.RESIDENCIAL) {
			throw new CoberturaNormativaException("A documentação (" + normaDoEnquadramento()
					+ ") não define o indicador Smart/Studio para edificação do tipo " + tipo + ".");
		}
	}

	private static String normaDoEnquadramento() {
		ReferenciaNormativa referencia = ParametroCalculo.LIMITE_BT_RESIDENCIAL_KVA.referencia();
		return referencia.norma() + " " + referencia.revisao();
	}

	private static void validarCoerenciaEntreTipoECargas(DadosEntradaCalculo entrada) {
		boolean possuiApartamentos = !entrada.apartamentos().isEmpty();
		switch (entrada.tipoEdificacao()) {
			case RESIDENCIAL -> {
				if (entrada.possuiCargaComercial()) {
					throw new EntradaInvalidaException(
							"Edificação RESIDENCIAL não pode ter carga comercial (Pcom ou condicionadores de ar); use MISTO.");
				}
			}
			case COMERCIAL -> {
				if (possuiApartamentos) {
					throw new EntradaInvalidaException("Edificação COMERCIAL não pode ter apartamentos; use MISTO.");
				}
			}
			case MISTO -> {
				if (!possuiApartamentos || !entrada.possuiCargaComercial()) {
					throw new EntradaInvalidaException(
							"Edificação MISTO precisa ter apartamentos e carga comercial (Pcom ou condicionadores de ar).");
				}
			}
			default -> throw new IllegalStateException("Tipo sem regra de coerência: " + entrada.tipoEdificacao());
		}
	}

}
