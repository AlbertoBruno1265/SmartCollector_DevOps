package com.example.smartcollector.controller;

import com.example.smartcollector.dto.ColetaRequest;
import com.example.smartcollector.model.Coleta;
import com.example.smartcollector.service.ColetaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/coletas")
public class ColetaController {

    private final ColetaService coletaService;

    public ColetaController(ColetaService coletaService) {
        this.coletaService = coletaService;
    }

    @GetMapping
    public ResponseEntity<List<Coleta>> listarTodas() {
        List<Coleta> coletas = coletaService.listarTodas();
        return ResponseEntity.ok(coletas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coleta> buscarPorId(
            @PathVariable
            @Positive(message = "O ID da coleta deve ser maior que zero")
            Long id
    ) {
        return coletaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Coleta> cadastrar(
            @Valid @RequestBody ColetaRequest request
    ) {
        Coleta coletaSalva = coletaService.salvar(request);
        return ResponseEntity.ok(coletaSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coleta> atualizar(
            @PathVariable
            @Positive(message = "O ID da coleta deve ser maior que zero")
            Long id,

            @Valid @RequestBody ColetaRequest request
    ) {
        Coleta coletaAtualizada = coletaService.atualizar(id, request);
        return ResponseEntity.ok(coletaAtualizada);
    }

    @PutMapping("/{idColeta}/aceitar/{idCatador}")
    public ResponseEntity<Coleta> aceitarColeta(
            @PathVariable
            @Positive(message = "O ID da coleta deve ser maior que zero")
            Long idColeta,

            @PathVariable
            @Positive(message = "O ID do catador deve ser maior que zero")
            Long idCatador
    ) {
        Coleta coletaAtualizada = coletaService.aceitarColeta(idColeta, idCatador);
        return ResponseEntity.ok(coletaAtualizada);
    }

    @PutMapping("/{idColeta}/finalizar/{idCentro}")
    public ResponseEntity<Coleta> finalizarColeta(
            @PathVariable
            @Positive(message = "O ID da coleta deve ser maior que zero")
            Long idColeta,

            @PathVariable
            @Positive(message = "O ID do centro de coleta deve ser maior que zero")
            Long idCentro
    ) {
        Coleta coletaFinalizada = coletaService.finalizarColeta(idColeta, idCentro);
        return ResponseEntity.ok(coletaFinalizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable
            @Positive(message = "O ID da coleta deve ser maior que zero")
            Long id
    ) {
        coletaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}