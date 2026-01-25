package br.com.alura.adopet.api.dto;

import jakarta.validation.constraints.NotNull;

public record CadastroPetDto(
        @NotNull
        Long idPet
) {
}
