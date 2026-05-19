package com.example.smartcollector.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ColetaRequest(
        @NotBlank(message = "O endereço é obrigatório")
        String endereco,

        @NotNull(message = "O ID do usuário é obrigatório")
        @Positive(message = "O ID do usuário deve ser maior que zero")
        Long idUsuario
) {}