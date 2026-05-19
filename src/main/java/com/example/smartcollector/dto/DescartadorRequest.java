package com.example.smartcollector.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record DescartadorRequest(
        @NotNull(message = "O ID do usuário é obrigatório")
        @Positive(message = "O ID do usuário deve ser maior que zero")
        Long idUsuario,

        @NotBlank(message = "O endereço é obrigatório")
        @Size(max = 150, message = "O endereço deve ter no máximo 150 caracteres")
        String endereco
) {}