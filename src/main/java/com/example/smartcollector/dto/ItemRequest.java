package com.example.smartcollector.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ItemRequest(
        @NotBlank(message = "O nome do item é obrigatório")
        @Size(max = 100, message = "O nome do item deve ter no máximo 100 caracteres")
        String nome,

        @NotNull(message = "O volume do item é obrigatório")
        @Positive(message = "O volume do item deve ser maior que zero")
        Double volume
) {}