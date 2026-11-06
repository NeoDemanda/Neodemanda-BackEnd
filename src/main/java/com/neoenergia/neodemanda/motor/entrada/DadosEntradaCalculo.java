package com.neoenergia.neodemanda.motor.entrada;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neoenergia.neodemanda.model.enums.TipoEdificacao;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record DadosEntradaCalculo(

		TipoEdificacao tipoEdificacao,

		@Positive(message = "deve ser maior que zero quando informada")
		Integer quantidadeUnidadesConsumidoras,

		@Positive(message = "deve ser maior que zero quando informada")
		BigDecimal cargaInstaladaKva,

		boolean condominioSmartStudio,

		List<@NotNull(message = "o item não pode ser nulo") @Valid ApartamentoEntrada> apartamentos,

		@PositiveOrZero(message = "não pode ser negativa")
		BigDecimal iluminacaoAreasComunsW,

		@PositiveOrZero(message = "não pode ser negativa")
		BigDecimal tomadasAreasComunsW,

		List<@NotNull(message = "o item não pode ser nulo") @Valid MotorEntrada> motores,

		@PositiveOrZero(message = "não pode ser negativa")
		BigDecimal cargaComercialKw,

		List<@NotNull(message = "o item não pode ser nulo") @Valid CondicionadorArEntrada> condicionadoresAr,

		List<@NotNull(message = "o item não pode ser nulo") @Valid EstacaoRecargaEntrada> estacoesRecarga,

		@NotNull(message = "é obrigatório")
		Boolean clienteGrupoA) {

	public DadosEntradaCalculo {
		Objects.requireNonNull(tipoEdificacao, "tipoEdificacao é obrigatório");
		apartamentos = copiar(apartamentos);
		iluminacaoAreasComunsW = Objects.requireNonNullElse(iluminacaoAreasComunsW, BigDecimal.ZERO);
		tomadasAreasComunsW = Objects.requireNonNullElse(tomadasAreasComunsW, BigDecimal.ZERO);
		motores = copiar(motores);
		cargaComercialKw = Objects.requireNonNullElse(cargaComercialKw, BigDecimal.ZERO);
		condicionadoresAr = copiar(condicionadoresAr);
		estacoesRecarga = copiar(estacoesRecarga);
	}

	public DadosEntradaCalculo(
			TipoEdificacao tipoEdificacao,
			Integer quantidadeUnidadesConsumidoras,
			BigDecimal cargaInstaladaKva) {
		this(tipoEdificacao, quantidadeUnidadesConsumidoras, cargaInstaladaKva, false, null, null, null, null, null,
				null, null, false);
	}

	public static Builder builder() {
		return new Builder();
	}

	@AssertTrue(message = "a edificação precisa ter pelo menos uma carga (apartamentos, serviços, comercial ou recarga)")
	public boolean isCargaInformada() {
		return !apartamentos.isEmpty()
				|| iluminacaoAreasComunsW.signum() > 0
				|| tomadasAreasComunsW.signum() > 0
				|| !motores.isEmpty()
				|| possuiCargaComercial()
				|| !estacoesRecarga.isEmpty();
	}

	public boolean possuiCargaComercial() {
		return cargaComercialKw.signum() > 0 || !condicionadoresAr.isEmpty();
	}

	public int totalApartamentos() {
		return apartamentos.stream().mapToInt(ApartamentoEntrada::quantidade).sum();
	}

	public int totalEstacoesRecarga() {
		return estacoesRecarga.stream().mapToInt(EstacaoRecargaEntrada::quantidade).sum();
	}

	private static <T> List<T> copiar(List<T> itens) {
		return itens == null ? List.of() : Collections.unmodifiableList(new ArrayList<>(itens));
	}

	public static final class Builder {

		private TipoEdificacao tipoEdificacao;

		private Integer quantidadeUnidadesConsumidoras;

		private BigDecimal cargaInstaladaKva;

		private boolean condominioSmartStudio;

		private final List<ApartamentoEntrada> apartamentos = new ArrayList<>();

		private BigDecimal iluminacaoAreasComunsW;

		private BigDecimal tomadasAreasComunsW;

		private final List<MotorEntrada> motores = new ArrayList<>();

		private BigDecimal cargaComercialKw;

		private final List<CondicionadorArEntrada> condicionadoresAr = new ArrayList<>();

		private final List<EstacaoRecargaEntrada> estacoesRecarga = new ArrayList<>();

		private Boolean clienteGrupoA;

		private Builder() {
		}

		public Builder tipoEdificacao(TipoEdificacao tipoEdificacao) {
			this.tipoEdificacao = tipoEdificacao;
			return this;
		}

		public Builder quantidadeUnidadesConsumidoras(Integer quantidadeUnidadesConsumidoras) {
			this.quantidadeUnidadesConsumidoras = quantidadeUnidadesConsumidoras;
			return this;
		}

		public Builder cargaInstaladaKva(BigDecimal cargaInstaladaKva) {
			this.cargaInstaladaKva = cargaInstaladaKva;
			return this;
		}

		public Builder condominioSmartStudio(boolean condominioSmartStudio) {
			this.condominioSmartStudio = condominioSmartStudio;
			return this;
		}

		public Builder apartamento(BigDecimal areaUtilM2, Integer quantidade) {
			this.apartamentos.add(new ApartamentoEntrada(areaUtilM2, quantidade));
			return this;
		}

		public Builder iluminacaoAreasComunsW(BigDecimal iluminacaoAreasComunsW) {
			this.iluminacaoAreasComunsW = iluminacaoAreasComunsW;
			return this;
		}

		public Builder tomadasAreasComunsW(BigDecimal tomadasAreasComunsW) {
			this.tomadasAreasComunsW = tomadasAreasComunsW;
			return this;
		}

		public Builder motor(BigDecimal potenciaCv, Integer quantidade) {
			this.motores.add(new MotorEntrada(potenciaCv, quantidade));
			return this;
		}

		public Builder cargaComercialKw(BigDecimal cargaComercialKw) {
			this.cargaComercialKw = cargaComercialKw;
			return this;
		}

		public Builder condicionadorAr(BigDecimal potencia, UnidadePotenciaAr unidadePotencia, Integer quantidade) {
			this.condicionadoresAr.add(new CondicionadorArEntrada(potencia, unidadePotencia, quantidade));
			return this;
		}

		public Builder estacaoRecarga(BigDecimal potenciaKw, Integer quantidade, TipoConexaoRecarga tipoConexao) {
			this.estacoesRecarga.add(new EstacaoRecargaEntrada(potenciaKw, quantidade, tipoConexao));
			return this;
		}

		public Builder clienteGrupoA(Boolean clienteGrupoA) {
			this.clienteGrupoA = clienteGrupoA;
			return this;
		}

		public DadosEntradaCalculo build() {
			return new DadosEntradaCalculo(tipoEdificacao, quantidadeUnidadesConsumidoras, cargaInstaladaKva,
					condominioSmartStudio, apartamentos, iluminacaoAreasComunsW, tomadasAreasComunsW, motores,
					cargaComercialKw, condicionadoresAr, estacoesRecarga, clienteGrupoA);
		}

	}

}
