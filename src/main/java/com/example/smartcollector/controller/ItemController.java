package com.example.smartcollector.controller;

import com.example.smartcollector.dto.ItemRequest;
import com.example.smartcollector.model.Item;
import com.example.smartcollector.service.ItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/itens")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> listarTodos() {
        List<Item> itens = itemService.listarTodos();
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> buscarPorId(
            @PathVariable
            @Positive(message = "O ID do item deve ser maior que zero")
            Long id
    ) {
        return itemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Item> cadastrar(
            @Valid @RequestBody ItemRequest request
    ) {
        Item itemSalvo = itemService.salvar(request);
        return ResponseEntity.ok(itemSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> atualizar(
            @PathVariable
            @Positive(message = "O ID do item deve ser maior que zero")
            Long id,

            @Valid @RequestBody ItemRequest request
    ) {
        Item itemAtualizado = itemService.atualizar(id, request);
        return ResponseEntity.ok(itemAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable
            @Positive(message = "O ID do item deve ser maior que zero")
            Long id
    ) {
        itemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}