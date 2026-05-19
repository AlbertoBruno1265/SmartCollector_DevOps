package com.example.smartcollector.controller;

import com.example.smartcollector.dto.DescartadorRequest;
import com.example.smartcollector.model.Descartador;
import com.example.smartcollector.service.DescartadorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/descartadores")
public class DescartadorController {

    private final DescartadorService descartadorService;

    public DescartadorController(DescartadorService descartadorService) {
        this.descartadorService = descartadorService;
    }

    @GetMapping
    public ResponseEntity<List<Descartador>> listarTodos() {
        List<Descartador> descartadores = descartadorService.listarTodos();
        return ResponseEntity.ok(descartadores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Descartador> buscarPorId(
            @PathVariable
            @Positive(message = "O ID do descartador deve ser maior que zero")
            Long id
    ) {
        return descartadorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Descartador> cadastrar(
            @Valid @RequestBody DescartadorRequest request
    ) {
        Descartador descartadorSalvo = descartadorService.salvar(request);
        return ResponseEntity.ok(descartadorSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Descartador> atualizar(
            @PathVariable
            @Positive(message = "O ID do descartador deve ser maior que zero")
            Long id,

            @Valid @RequestBody DescartadorRequest request
    ) {
        Descartador descartadorAtualizado = descartadorService.atualizar(id, request);
        return ResponseEntity.ok(descartadorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable
            @Positive(message = "O ID do descartador deve ser maior que zero")
            Long id
    ) {
        descartadorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}