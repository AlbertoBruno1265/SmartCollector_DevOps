package com.example.smartcollector.service;

import com.example.smartcollector.dto.CentroColetaRequest;
import com.example.smartcollector.dto.CentroColetaResponse;
import com.example.smartcollector.model.CentroColeta;
import com.example.smartcollector.repository.CentroColetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CentroColetaService {

    private final CentroColetaRepository centroColetaRepository;

    public CentroColetaService(CentroColetaRepository centroColetaRepository) {
        this.centroColetaRepository = centroColetaRepository;
    }

    public List<CentroColetaResponse> listarTodos() {
        return centroColetaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CentroColetaResponse buscarPorId(Long id) {
        CentroColeta centroColeta = centroColetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Centro de coleta não encontrado"));

        return toResponse(centroColeta);
    }

    public CentroColetaResponse salvar(CentroColetaRequest request) {
        CentroColeta centroColeta = new CentroColeta();

        centroColeta.setEndereco(request.endereco());
        centroColeta.setVolumeItensTotal(request.volumeItensTotal());
        centroColeta.setVolumeItensAtual(request.volumeItensAtual());

        CentroColeta centroSalvo = centroColetaRepository.save(centroColeta);

        return toResponse(centroSalvo);
    }

    public CentroColetaResponse atualizar(Long id, CentroColetaRequest request) {
        CentroColeta centroExistente = centroColetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Centro de coleta não encontrado"));

        centroExistente.setEndereco(request.endereco());
        centroExistente.setVolumeItensTotal(request.volumeItensTotal());
        centroExistente.setVolumeItensAtual(request.volumeItensAtual());

        CentroColeta centroAtualizado = centroColetaRepository.save(centroExistente);

        return toResponse(centroAtualizado);
    }

    public void deletar(Long id) {
        if (!centroColetaRepository.existsById(id)) {
            throw new RuntimeException("Centro de coleta não encontrado");
        }

        centroColetaRepository.deleteById(id);
    }

    private CentroColetaResponse toResponse(CentroColeta centroColeta) {
        return new CentroColetaResponse(
                centroColeta.getId(),
                centroColeta.getEndereco(),
                centroColeta.getVolumeItensTotal(),
                centroColeta.getVolumeItensAtual()
        );
    }
}