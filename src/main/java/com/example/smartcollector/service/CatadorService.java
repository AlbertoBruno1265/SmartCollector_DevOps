package com.example.smartcollector.service;

import com.example.smartcollector.dto.CatadorRequest;
import com.example.smartcollector.dto.CatadorResponse;
import com.example.smartcollector.model.Catador;
import com.example.smartcollector.model.Usuario;
import com.example.smartcollector.repository.CatadorRepository;
import com.example.smartcollector.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CatadorService {

    private final CatadorRepository catadorRepository;
    private final UsuarioRepository usuarioRepository;

    public CatadorService(
            CatadorRepository catadorRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.catadorRepository = catadorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<CatadorResponse> listarTodos() {

        return catadorRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    public Optional<CatadorResponse> buscarPorId(Long id) {

        return catadorRepository.findById(id)
                .map(this::converterParaResponse);
    }

    public CatadorResponse salvar(CatadorRequest request) {

        Usuario usuario = usuarioRepository.findById(
                request.getUsuarioId()
        ).orElseThrow(() ->
                new RuntimeException("Usuário não encontrado")
        );

        Catador catador = new Catador();

        catador.setCapacidadeVolumeTotal(
                request.getCapacidadeVolumeTotal()
        );

        catador.setUsuario(usuario);

        Catador salvo = catadorRepository.save(catador);

        return converterParaResponse(salvo);
    }

    public CatadorResponse atualizar(
            Long id,
            CatadorRequest request
    ) {

        Catador catadorExistente = catadorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Catador não encontrado")
                );

        catadorExistente.setCapacidadeVolumeTotal(
                request.getCapacidadeVolumeTotal()
        );

        Catador atualizado =
                catadorRepository.save(catadorExistente);

        return converterParaResponse(atualizado);
    }

    public void deletar(Long id) {

        if (!catadorRepository.existsById(id)) {
            throw new RuntimeException("Catador não encontrado");
        }

        catadorRepository.deleteById(id);
    }

    private CatadorResponse converterParaResponse(
            Catador catador
    ) {

        return new CatadorResponse(
                catador.getId(),
                catador.getUsuario().getId(),
                catador.getCapacidadeVolumeTotal()
        );
    }
}