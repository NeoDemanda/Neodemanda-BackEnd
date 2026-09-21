package com.neoenergia.neodemanda.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import com.neoenergia.neodemanda.model.enums.StatusProjeto;
import com.neoenergia.neodemanda.model.enums.TipoEdificacao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


@Entity
@Table(name = "projeto")
public class Projeto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "nome_projeto", nullable = false, length = 150)
	private String nomeProjeto;

	@NotBlank
	@Column(name = "responsavel_tecnico", nullable = false, length = 150)
	private String responsavelTecnico;

	@NotBlank
	@Column(name = "crea_responsavel", nullable = false, length = 30)
	private String creaResponsavel;

	@NotNull
	@Positive
	@Column(name = "quantidade_unidades_consumidoras", nullable = false)
	private Integer quantidadeUnidadesConsumidoras;

	@NotNull
	@Positive
	@Column(name = "carga_instalada_kva", nullable = false, precision = 12, scale = 3)
	private BigDecimal cargaInstaladaKva;

	
	@Positive
	@Column(name = "demanda_calculada_kva", precision = 12, scale = 3)
	private BigDecimal demandaCalculadaKva;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_edificacao", nullable = false, length = 30)
	private TipoEdificacao tipoEdificacao;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 30)
	private StatusProjeto status;

	@Column(name = "data_criacao", nullable = false, updatable = false)
	private LocalDateTime dataCriacao;

	@Column(name = "data_atualizacao", nullable = false)
	private LocalDateTime dataAtualizacao;

	
	public Projeto() {
	}

	
	public Projeto(String nomeProjeto,
			String responsavelTecnico,
			String creaResponsavel,
			Integer quantidadeUnidadesConsumidoras,
			BigDecimal cargaInstaladaKva,
			BigDecimal demandaCalculadaKva,
			TipoEdificacao tipoEdificacao,
			StatusProjeto status) {
		this.nomeProjeto = nomeProjeto;
		this.responsavelTecnico = responsavelTecnico;
		this.creaResponsavel = creaResponsavel;
		this.quantidadeUnidadesConsumidoras = quantidadeUnidadesConsumidoras;
		this.cargaInstaladaKva = cargaInstaladaKva;
		this.demandaCalculadaKva = demandaCalculadaKva;
		this.tipoEdificacao = tipoEdificacao;
		this.status = status;
	}

	@PrePersist
	void aoCriar() {
		LocalDateTime agora = LocalDateTime.now();
		this.dataCriacao = agora;
		this.dataAtualizacao = agora;
	}

	@PreUpdate
	void aoAtualizar() {
		this.dataAtualizacao = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}

	public String getResponsavelTecnico() {
		return responsavelTecnico;
	}

	public void setResponsavelTecnico(String responsavelTecnico) {
		this.responsavelTecnico = responsavelTecnico;
	}

	public String getCreaResponsavel() {
		return creaResponsavel;
	}

	public void setCreaResponsavel(String creaResponsavel) {
		this.creaResponsavel = creaResponsavel;
	}

	public Integer getQuantidadeUnidadesConsumidoras() {
		return quantidadeUnidadesConsumidoras;
	}

	public void setQuantidadeUnidadesConsumidoras(Integer quantidadeUnidadesConsumidoras) {
		this.quantidadeUnidadesConsumidoras = quantidadeUnidadesConsumidoras;
	}

	public BigDecimal getCargaInstaladaKva() {
		return cargaInstaladaKva;
	}

	public void setCargaInstaladaKva(BigDecimal cargaInstaladaKva) {
		this.cargaInstaladaKva = cargaInstaladaKva;
	}

	public BigDecimal getDemandaCalculadaKva() {
		return demandaCalculadaKva;
	}

	public void setDemandaCalculadaKva(BigDecimal demandaCalculadaKva) {
		this.demandaCalculadaKva = demandaCalculadaKva;
	}

	public TipoEdificacao getTipoEdificacao() {
		return tipoEdificacao;
	}

	public void setTipoEdificacao(TipoEdificacao tipoEdificacao) {
		this.tipoEdificacao = tipoEdificacao;
	}

	public StatusProjeto getStatus() {
		return status;
	}

	public void setStatus(StatusProjeto status) {
		this.status = status;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDateTime getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	
	@Override
	public boolean equals(Object outro) {
		if (this == outro) {
			return true;
		}
		if (!(outro instanceof Projeto projeto)) {
			return false;
		}
		return id != null && Objects.equals(id, projeto.id);
	}

	
	@Override
	public int hashCode() {
		return Projeto.class.hashCode();
	}

	@Override
	public String toString() {
		return "Projeto{" +
				"id=" + id +
				", nomeProjeto='" + nomeProjeto + '\'' +
				", responsavelTecnico='" + responsavelTecnico + '\'' +
				", creaResponsavel='" + creaResponsavel + '\'' +
				", quantidadeUnidadesConsumidoras=" + quantidadeUnidadesConsumidoras +
				", cargaInstaladaKva=" + cargaInstaladaKva +
				", demandaCalculadaKva=" + demandaCalculadaKva +
				", tipoEdificacao=" + tipoEdificacao +
				", status=" + status +
				", dataCriacao=" + dataCriacao +
				", dataAtualizacao=" + dataAtualizacao +
				'}';
	}

}
