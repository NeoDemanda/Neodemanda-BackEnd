package com.neoenergia.neodemanda.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.neoenergia.neodemanda.dto.ProjetoRequestDTO;
import com.neoenergia.neodemanda.dto.ProjetoResponseDTO;
import com.neoenergia.neodemanda.exception.ConflictException;
import com.neoenergia.neodemanda.exception.ResourceNotFoundException;
import com.neoenergia.neodemanda.model.Projeto;
import com.neoenergia.neodemanda.model.enums.StatusProjeto;

@Service
public class ProjetoService {

    private static final Set<StatusProjeto> STATUS_EDITAVEIS = Set.of(StatusProjeto.RASCUNHO, StatusProjeto.EM_CORRECAO);
    private static final Set<StatusProjeto> STATUS_EXCLUIVEIS = Set.of(StatusProjeto.RASCUNHO);

    private final AtomicLong sequenciaId = new AtomicLong();
    private final List<Projeto> projetosEmMemoria = Collections.synchronizedList(new ArrayList<>());
    private final Map<Long, Long> revisoes = new ConcurrentHashMap<>();

   
    public synchronized ProjetoResponseDTO cadastrar(ProjetoRequestDTO requisicao) {
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

    
    public synchronized List<ProjetoResponseDTO> listarTodos() {
        return projetosEmMemoria.stream()
                .filter(projeto -> projeto.getStatus() != StatusProjeto.ARQUIVADO)
                .map(this::toResponseDTO)
                .toList();
    }


    public synchronized List<ProjetoResponseDTO> listar(StatusProjeto status) {
        if (status == null) {
            return listarTodos();
        }
        return projetosEmMemoria.stream()
                .filter(projeto -> projeto.getStatus() == status)
                .map(this::toResponseDTO)
                .toList();
    }


    public synchronized ProjetoResponseDTO consultar(Long id) {
        return toResponseDTO(buscarPorId(id));
    }


    public synchronized ProjetoResponseDTO atualizar(Long id, ProjetoRequestDTO requisicao) {
        Projeto projeto = buscarPorId(id);
        validarAlteracaoPermitida(projeto, "editado", STATUS_EDITAVEIS);

        projeto.setNomeProjeto(requisicao.nomeProjeto());
        projeto.setResponsavelTecnico(requisicao.responsavelTecnico());
        projeto.setCreaResponsavel(requisicao.creaResponsavel());
        projeto.setQuantidadeUnidadesConsumidoras(requisicao.quantidadeUnidadesConsumidoras());
        projeto.setCargaInstaladaKva(requisicao.cargaInstaladaKva());
        projeto.setTipoEdificacao(requisicao.tipoEdificacao());
        revisoes.merge(id, 1L, Long::sum);

        return toResponseDTO(projeto);
    }


    public synchronized ProjetoResponseDTO arquivar(Long id) {
        Projeto projeto = buscarPorId(id);
        if (projeto.getStatus() == StatusProjeto.ARQUIVADO) {
            throw new ConflictException("Projeto ja esta arquivado");
        }

        projeto.setStatus(StatusProjeto.ARQUIVADO);

        return toResponseDTO(projeto);
    }


    public synchronized void validarTransicao(Long id, StatusProjeto destino) {
        StatusProjeto atual = buscarPorId(id).getStatus();
        if (!atual.podeTransicionarPara(destino)) {
            throw new ConflictException("Transicao invalida: projeto em " + atual + " nao pode passar para " + destino);
        }
    }


    public synchronized ProjetoResponseDTO transicionar(Long id, StatusProjeto destino) {
        validarTransicao(id, destino);
        Projeto projeto = buscarPorId(id);
        projeto.setStatus(destino);

        return toResponseDTO(projeto);
    }


    public synchronized <T> T executarComLock(Supplier<T> acao) {
        return acao.get();
    }


    public synchronized void excluir(Long id) {
        Projeto projeto = buscarPorId(id);
        validarAlteracaoPermitida(projeto, "excluido", STATUS_EXCLUIVEIS);

        projetosEmMemoria.remove(projeto);
    }


    public synchronized ProjetoParaCalculo consultarParaCalculo(Long id) {
        return paraCalculo(buscarPorId(id));
    }


    public synchronized ProjetoParaCalculo consultarParaNovoCalculo(Long id) {
        Projeto projeto = buscarPorId(id);
        validarAlteracaoPermitida(projeto, "calculado", STATUS_EDITAVEIS);

        return paraCalculo(projeto);
    }


    private ProjetoParaCalculo paraCalculo(Projeto projeto) {
        return new ProjetoParaCalculo(
                projeto.getTipoEdificacao(),
                projeto.getQuantidadeUnidadesConsumidoras(),
                projeto.getCargaInstaladaKva(),
                revisoes.getOrDefault(projeto.getId(), 0L));
    }


    private synchronized Projeto buscarPorId(Long id) {
        return projetosEmMemoria.stream()
                .filter(projeto -> projeto.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Projeto nao encontrado: " + id));
    }


    private void validarAlteracaoPermitida(Projeto projeto, String acao, Set<StatusProjeto> statusPermitidos) {
        if (projeto.getStatus() == StatusProjeto.ARQUIVADO) {
            throw new ConflictException("Projeto arquivado nao pode ser " + acao);
        }
        if (!statusPermitidos.contains(projeto.getStatus())) {
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