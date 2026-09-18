package com.neoenergia.neodemanda.service;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.neoenergia.neodemanda.dto.ProjetoRequestDTO;
import com.neoenergia.neodemanda.dto.ProjetoResponseDTO;
import com.neoenergia.neodemanda.model.Projeto;
import com.neoenergia.neodemanda.model.enums.StatusProjeto;

/**
 * Regras de cadastro de projeto eletrico.
 *
 * <p>Nesta etapa a entidade e apenas montada em memoria. Todo o contato com o
 * armazenamento esta confinado em {@link #persistir(Projeto)}: quando o
 * repositorio JPA entrar, basta trocar o corpo desse metodo, sem tocar em
 * {@link #cadastrar(ProjetoRequestDTO)}.
 */
@Service
public class ProjetoService {

	private final AtomicLong sequenciaId = new AtomicLong();

	/**
	 * Converte a requisicao em {@link Projeto}, atribui o status inicial
	 * {@link StatusProjeto#RASCUNHO} e devolve a representacao cadastrada.
	 */
	public ProjetoResponseDTO cadastrar(ProjetoRequestDTO requisicao) {
		Projeto projeto = new Projeto(
				requisicao.nomeProjeto(),
				requisicao.responsavelTecnico(),
				requisicao.creaResponsavel(),
				requisicao.quantidadeUnidadesConsumidoras(),
				requisicao.cargaInstaladaKva(),
				null,
				requisicao.tipoEdificacao(),
				StatusProjeto.RASCUNHO);

		Projeto cadastrado = persistir(projeto);

		return new ProjetoResponseDTO(
				cadastrado.getId(),
				cadastrado.getNomeProjeto(),
				cadastrado.getResponsavelTecnico(),
				cadastrado.getCreaResponsavel(),
				cadastrado.getQuantidadeUnidadesConsumidoras(),
				cadastrado.getCargaInstaladaKva(),
				cadastrado.getTipoEdificacao(),
				cadastrado.getStatus());
	}

	/**
	 * Unico ponto de persistencia do cadastro.
	 *
	 * <p>Sem banco nesta etapa: o identificador vem de uma sequencia em memoria,
	 * valida apenas dentro da instancia em execucao, e a entidade nao e
	 * armazenada. Substituir por {@code projetoRepository.save(projeto)}.
	 */
	private Projeto persistir(Projeto projeto) {
		projeto.setId(sequenciaId.incrementAndGet());
		return projeto;
	}

}
