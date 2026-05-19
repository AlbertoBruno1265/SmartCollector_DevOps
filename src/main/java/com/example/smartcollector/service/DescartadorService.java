package com.example.smartcollector.service;

import com.example.smartcollector.dto.DescartadorRequest;
import com.example.smartcollector.model.Descartador;
import com.example.smartcollector.model.Usuario;
import com.example.smartcollector.repository.DescartadorRepository;
import com.example.smartcollector.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DescartadorService {

    private final DescartadorRepository descartadorRepository;
    private final UsuarioRepository usuarioRepository;

    public DescartadorService(DescartadorRepository descartadorRepository, UsuarioRepository usuarioRepository) {
        this.descartadorRepository = descartadorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Descartador> listarTodos() {
        return descartadorRepository.findAll();
    }

    public Optional<Descartador> buscarPorId(Long id) {
        return descartadorRepository.findById(id);
    }

    public Descartador salvar(DescartadorRequest request) {
        Long usuarioId = request.idUsuario();

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Descartador descartador = new Descartador();
        descartador.setUsuario(usuario);
        descartador.setEndereco(request.endereco());

        return descartadorRepository.save(descartador);
    }

    public Descartador atualizar(Long id, DescartadorRequest request) {
        Descartador descartadorExistente = descartadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Descartador não encontrado"));

        descartadorExistente.setEndereco(request.endereco());

        return descartadorRepository.save(descartadorExistente);
    }

    public void deletar(Long id) {
        if (!descartadorRepository.existsById(id)) {
            throw new RuntimeException("Descartador não encontrado");
        }

        descartadorRepository.deleteById(id);
    }
}