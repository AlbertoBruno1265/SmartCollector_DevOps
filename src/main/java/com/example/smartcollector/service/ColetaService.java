package com.example.smartcollector.service;

import com.example.smartcollector.dto.ColetaRequest;
import com.example.smartcollector.model.Catador;
import com.example.smartcollector.model.CentroColeta;
import com.example.smartcollector.model.Coleta;
import com.example.smartcollector.model.Descartador;
import com.example.smartcollector.repository.CatadorRepository;
import com.example.smartcollector.repository.CentroColetaRepository;
import com.example.smartcollector.repository.ColetaRepository;
import com.example.smartcollector.repository.DescartadorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ColetaService {

    private final ColetaRepository coletaRepository;
    private final DescartadorRepository descartadorRepository;
    private final CatadorRepository catadorRepository;
    private final CentroColetaRepository centroColetaRepository;

    public ColetaService(
            ColetaRepository coletaRepository,
            DescartadorRepository descartadorRepository,
            CatadorRepository catadorRepository,
            CentroColetaRepository centroColetaRepository
    ) {
        this.coletaRepository = coletaRepository;
        this.descartadorRepository = descartadorRepository;
        this.catadorRepository = catadorRepository;
        this.centroColetaRepository = centroColetaRepository;
    }

    public List<Coleta> listarTodas() {
        return coletaRepository.findAll();
    }

    public Optional<Coleta> buscarPorId(Long id) {
        return coletaRepository.findById(id);
    }

    public Coleta salvar(ColetaRequest request) {
        Long descartadorId = request.idUsuario();

        Descartador descartador = descartadorRepository.findById(descartadorId)
                .orElseThrow(() -> new RuntimeException("Descartador não encontrado"));

        Coleta coleta = new Coleta();
        coleta.setDescartador(descartador);
        coleta.setData(LocalDateTime.now());
        coleta.setFoiFinalizada(false);

        return coletaRepository.save(coleta);
    }

    public Coleta atualizar(Long id, ColetaRequest request) {
        Coleta coletaExistente = coletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coleta não encontrada"));

        Long descartadorId = request.idUsuario();

        Descartador descartador = descartadorRepository.findById(descartadorId)
                .orElseThrow(() -> new RuntimeException("Descartador não encontrado"));

        coletaExistente.setDescartador(descartador);

        return coletaRepository.save(coletaExistente);
    }

    public Coleta aceitarColeta(Long idColeta, Long idCatador) {
        Coleta coleta = coletaRepository.findById(idColeta)
                .orElseThrow(() -> new RuntimeException("Coleta não encontrada"));

        Catador catador = catadorRepository.findById(idCatador)
                .orElseThrow(() -> new RuntimeException("Catador não encontrado"));

        coleta.setCatador(catador);

        return coletaRepository.save(coleta);
    }

    public Coleta finalizarColeta(Long idColeta, Long idCentro) {
        Coleta coleta = coletaRepository.findById(idColeta)
                .orElseThrow(() -> new RuntimeException("Coleta não encontrada"));

        CentroColeta centroColeta = centroColetaRepository.findById(idCentro)
                .orElseThrow(() -> new RuntimeException("Centro de coleta não encontrado"));

        coleta.setCentroColeta(centroColeta);
        coleta.setFoiFinalizada(true);

        return coletaRepository.save(coleta);
    }

    public void deletar(Long id) {
        if (!coletaRepository.existsById(id)) {
            throw new RuntimeException("Coleta não encontrada");
        }

        coletaRepository.deleteById(id);
    }
}