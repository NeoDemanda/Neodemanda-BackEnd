package com.neoenergia.neodemanda.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ParecerObrigatorioValidator.class)
public @interface ParecerObrigatorio {

	String message() default "O parecer e obrigatorio para as decisoes EM_CORRECAO e REPROVADO";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
