package com.neoenergia.neodemanda.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.neoenergia.neodemanda.dto.ProjetoRequestDTO;
import com.neoenergia.neodemanda.dto.ProjetoResponseDTO;
import com.neoenergia.neodemanda.exception.ConflictException;
import com.neoenergia.neodemanda.exception.ResourceNotFoundException;
import com.neoenergia.neodemanda.model.Projeto;
import com.neoenergia.neodemanda.model.enums.StatusProjeto;

@Service
public class ProjetoService {

    private final AtomicLong sequenciaId = new AtomicLong();
    private final List<Projeto> projetosEmMemoria = Collections.synchronizedList(new ArrayList<>());

   
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

        return toResponseDTO(cadastrado);
    }

    
    public List<ProjetoResponseDTO> listarTodos() {
        return projetosEmMemoria.stream()
                .filter(projeto -> projeto.getStatus() != StatusProjeto.ARQUIVADO)
                .map(this::toResponseDTO)
                .toList();
    }


    public ProjetoResponseDTO atualizar(Long id, ProjetoRequestDTO requisicao) {
        Projeto projeto = buscarPorId(id);
        validarAlteracaoPermitida(projeto, "editado");

        projeto.setNomeProjeto(requisicao.nomeProjeto());
        projeto.setResponsavelTecnico(requisicao.responsavelTecnico());
        projeto.setCreaResponsavel(requisicao.creaResponsavel());
        projeto.setQuantidadeUnidadesConsumidoras(requisicao.quantidadeUnidadesConsumidoras());
        projeto.setCargaInstaladaKva(requisicao.cargaInstaladaKva());
        projeto.setTipoEdificacao(requisicao.tipoEdificacao());

        return toResponseDTO(projeto);
    }


    public ProjetoResponseDTO arquivar(Long id) {
        Projeto projeto = buscarPorId(id);
        if (projeto.getStatus() == StatusProjeto.ARQUIVADO) {
            throw new ConflictException("Projeto ja esta arquivado");
        }

        projeto.setStatus(StatusProjeto.ARQUIVADO);

        return toResponseDTO(projeto);
    }


    public void excluir(Long id) {
        Projeto projeto = buscarPorId(id);
        validarAlteracaoPermitida(projeto, "excluido");

        projetosEmMemoria.remove(projeto);
    }


    private Projeto buscarPorId(Long id) {
        return projetosEmMemoria.stream()
                .filter(projeto -> projeto.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Projeto nao encontrado: " + id));
    }


    private void validarAlteracaoPermitida(Projeto projeto, String acao) {
        if (projeto.getStatus() == StatusProjeto.ARQUIVADO) {
            throw new ConflictException("Projeto arquivado nao pode ser " + acao);
        }
        if (projeto.getStatus() != StatusProjeto.RASCUNHO) {
            throw new ConflictException("Projeto submetido nao pode ser " + acao
                    + ". Status atual: " + projeto.getStatus());
        }
    }


    private ProjetoResponseDTO toResponseDTO(Projeto projeto) {
        return new ProjetoResponseDTO(
                projeto.getId(),
                projeto.getNomeProjeto(),
                projeto.getResponsavelTecnico(),
                projeto.getCreaResponsavel(),
                projeto.getQuantidadeUnidadesConsumidoras(),
                projeto.getCargaInstaladaKva(),
                projeto.getTipoEdificacao(),
                projeto.getStatus());
    }


    private Projeto persistir(Projeto projeto) {
        projeto.setId(sequenciaId.incrementAndGet());
        projetosEmMemoria.add(projeto);
        return projeto;
    }

}