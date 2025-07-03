package it.epicode.CapstoneProjectBackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


    @Data
    public class FavoriteDto {
        @NotNull(message = "L'id della canzone è obbligatorio")
        private int songId;
    }
