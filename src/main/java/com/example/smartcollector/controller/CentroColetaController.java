package com.example.smartcollector.controller;

import com.example.smartcollector.dto.CentroColetaRequest;
import com.example.smartcollector.dto.CentroColetaResponse;
import com.example.smartcollector.service.CentroColetaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/centros-coleta")
public class CentroColetaController {

    private final CentroColetaService centroColetaService;

    public CentroColetaController(CentroColetaService centroColetaService) {
        this.centroColetaService = centroColetaService;
    }

    @GetMapping
    public ResponseEntity<List<CentroColetaResponse>> listarTodos() {
        return ResponseEntity.ok(centroColetaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CentroColetaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(centroColetaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CentroColetaResponse> salvar(
            @Valid @RequestBody CentroColetaRequest request
    ) {
        return ResponseEntity.ok(centroColetaService.salvar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CentroColetaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CentroColetaRequest request
    ) {
        return ResponseEntity.ok(centroColetaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        centroColetaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}