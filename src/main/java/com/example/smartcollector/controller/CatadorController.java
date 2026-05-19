package com.example.smartcollector.controller;

import com.example.smartcollector.service.CatadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.smartcollector.dto.CatadorRequest;
import com.example.smartcollector.dto.CatadorResponse;
import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/catadores")
public class CatadorController {

    private final CatadorService catadorService;

    public CatadorController(CatadorService catadorService) {
        this.catadorService = catadorService;
    }

    @GetMapping
    public ResponseEntity<List<CatadorResponse>> listarTodos() {
        return ResponseEntity.ok(catadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatadorResponse> buscarPorId(
            @PathVariable Long id
    ) {

        return catadorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CatadorResponse> cadastrar(
            @Valid @RequestBody CatadorRequest request
    ) {

        CatadorResponse response =
                catadorService.salvar(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatadorResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CatadorRequest request
    ) {

        CatadorResponse response =
                catadorService.atualizar(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        catadorService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}