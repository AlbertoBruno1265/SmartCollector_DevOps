package com.example.smartcollector.service;

import com.example.smartcollector.dto.ItemRequest;
import com.example.smartcollector.model.Item;
import com.example.smartcollector.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> listarTodos() {
        return itemRepository.findAll();
    }

    public Optional<Item> buscarPorId(Long id) {
        return itemRepository.findById(id);
    }

    public Item salvar(ItemRequest request) {
        Item item = new Item();
        item.setNome(request.nome());
        item.setVolume(request.volume());

        return itemRepository.save(item);
    }

    public Item atualizar(Long id, ItemRequest request) {
        Item itemExistente = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        itemExistente.setNome(request.nome());
        itemExistente.setVolume(request.volume());

        return itemRepository.save(itemExistente);
    }

    public void deletar(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item não encontrado");
        }

        itemRepository.deleteById(id);
    }
}