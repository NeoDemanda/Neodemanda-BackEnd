package com.neoenergia.neodemanda.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.neoenergia.neodemanda.dto.AvaliacaoRequest;
import com.neoenergia.neodemanda.dto.AvaliacaoResponse;
import com.neoenergia.neodemanda.dto.ProjetoAvaliadoResponse;
import com.neoenergia.neodemanda.dto.ProjetoResponseDTO;
import com.neoenergia.neodemanda.exception.ConflictException;
import com.neoenergia.neodemanda.model.Avaliacao;
import com.neoenergia.neodemanda.model.enums.StatusProjeto;
import com.neoenergia.neodemanda.repository.AvaliacaoRepository;

@Service
public class AvaliacaoService {

	private final ProjetoService projetoService;

	private final CalculoService calculoService;

	private final AvaliacaoRepository avaliacaoRepository;

	public AvaliacaoService(ProjetoService projetoService, CalculoService calculoService,
			AvaliacaoRepository avaliacaoRepository) {
		this.projetoService = projetoService;
		this.calculoService = calculoService;
		this.avaliacaoRepository = avaliacaoRepository;
	}

	public ProjetoResponseDTO submeter(Long projetoId) {
		return projetoService.executarComLock(() -> {
			projetoService.validarTransicao(projetoId, StatusProjeto.EM_ANALISE);
			if (!calculoService.temCalculoValido(projetoId)) {
				throw new ConflictException("Projeto sem calculo valido. Calcule a demanda antes de submeter.");
			}
			return projetoService.transicionar(projetoId, StatusProjeto.EM_ANALISE);
		});
	}

	public ProjetoAvaliadoResponse avaliar(Long projetoId, AvaliacaoRequest requisicao) {
		return projetoService.executarComLock(() -> {
			ProjetoResponseDTO projeto = projetoService.transicionar(projetoId, requisicao.decisao().statusDestino());
			Avaliacao avaliacao = new Avaliacao(projetoId, requisicao.decisao(), requisicao.parecer(), OffsetDateTime.now());
			avaliacaoRepository.salvar(avaliacao);

			return new ProjetoAvaliadoResponse(projeto, AvaliacaoResponse.de(avaliacao));
		});
	}

	public List<AvaliacaoResponse> listarAvaliacoes(Long projetoId) {
		projetoService.consultar(projetoId);

		return avaliacaoRepository.listarPorProjeto(projetoId).stream()
				.map(AvaliacaoResponse::de)
				.toList();
	}

}
