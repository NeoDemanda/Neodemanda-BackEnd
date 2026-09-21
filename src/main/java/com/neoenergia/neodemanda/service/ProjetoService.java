package com.neoenergia.neodemanda.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.neoenergia.neodemanda.dto.ProjetoRequestDTO;
import com.neoenergia.neodemanda.dto.ProjetoResponseDTO;
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
                .map(this::toResponseDTO)
                .toList();
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