package com.example.smartcollector.controller;

import com.example.smartcollector.dto.ColetaItemRequest;
import com.example.smartcollector.model.ColetaItem;
import com.example.smartcollector.service.ColetaItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/coleta-itens")
public class ColetaItemController {

    private final ColetaItemService coletaItemService;

    public ColetaItemController(ColetaItemService coletaItemService) {
        this.coletaItemService = coletaItemService;
    }

    @GetMapping
    public ResponseEntity<List<ColetaItem>> listarTodos() {
        List<ColetaItem> coletaItens = coletaItemService.listarTodos();
        return ResponseEntity.ok(coletaItens);
    }

    @GetMapping("/{idColeta}/{idItem}")
    public ResponseEntity<ColetaItem> buscarPorId(
            @PathVariable
            @Positive(message = "O ID da coleta deve ser maior que zero")
            Long idColeta,

            @PathVariable
            @Positive(message = "O ID do item deve ser maior que zero")
            Long idItem
    ) {
        return coletaItemService.buscarPorId(idColeta, idItem)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ColetaItem> cadastrar(
            @Valid @RequestBody ColetaItemRequest request
    ) {
        ColetaItem coletaItemSalvo = coletaItemService.salvar(request);
        return ResponseEntity.ok(coletaItemSalvo);
    }

    @DeleteMapping("/{idColeta}/{idItem}")
    public ResponseEntity<Void> deletar(
            @PathVariable
            @Positive(message = "O ID da coleta deve ser maior que zero")
            Long idColeta,

            @PathVariable
            @Positive(message = "O ID do item deve ser maior que zero")
            Long idItem
    ) {
        coletaItemService.deletar(idColeta, idItem);
        return ResponseEntity.noContent().build();
    }
}