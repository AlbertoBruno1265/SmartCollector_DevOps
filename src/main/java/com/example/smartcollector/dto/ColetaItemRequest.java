package com.example.smartcollector.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ColetaItemRequest(
        @NotNull(message = "O ID da coleta é obrigatório")
        @Positive(message = "O ID da coleta deve ser maior que zero")
        Long idColeta,

        @NotNull(message = "O ID do item é obrigatório")
        @Positive(message = "O ID do item deve ser maior que zero")
        Long idItem
) {}