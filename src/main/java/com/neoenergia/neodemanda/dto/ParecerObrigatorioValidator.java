package com.neoenergia.neodemanda.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ParecerObrigatorioValidator implements ConstraintValidator<ParecerObrigatorio, AvaliacaoRequest> {

	@Override
	public boolean isValid(AvaliacaoRequest requisicao, ConstraintValidatorContext contexto) {
		if (requisicao.decisao() == null || !requisicao.decisao().exigeParecer() || requisicao.parecer() != null) {
			return true;
		}
		contexto.disableDefaultConstraintViolation();
		contexto.buildConstraintViolationWithTemplate(contexto.getDefaultConstraintMessageTemplate())
				.addPropertyNode("parecer")
				.addConstraintViolation();
		return false;
	}

}
