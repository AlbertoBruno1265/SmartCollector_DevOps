package com.example.smartcollector.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CatadorRequest {

    @NotNull(message = "Usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "Capacidade é obrigatória")
    @Positive(message = "Capacidade deve ser positiva")
    private Double capacidadeVolumeTotal;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Double getCapacidadeVolumeTotal() {
        return capacidadeVolumeTotal;
    }

    public void setCapacidadeVolumeTotal(Double capacidadeVolumeTotal) {
        this.capacidadeVolumeTotal = capacidadeVolumeTotal;
    }
}