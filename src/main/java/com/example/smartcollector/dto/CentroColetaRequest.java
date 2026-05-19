package com.example.smartcollector.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CentroColetaRequest(
        @NotBlank(message = "O endereço é obrigatório")
        String endereco,

        @NotNull(message = "O volume total é obrigatório")
        @Positive(message = "O volume total deve ser maior que zero")
        Double volumeItensTotal,

        @NotNull(message = "O volume atual é obrigatório")
        @PositiveOrZero(message = "O volume atual não pode ser negativo")
        Double volumeItensAtual
) {
    @AssertTrue(message = "O volume atual não pode ser maior que o volume total")
    public boolean isVolumeAtualValido() {
        if (volumeItensAtual == null || volumeItensTotal == null) {
            return true;
        }

        return volumeItensAtual <= volumeItensTotal;
    }
}