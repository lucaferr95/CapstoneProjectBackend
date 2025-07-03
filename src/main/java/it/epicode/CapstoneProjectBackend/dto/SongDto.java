package it.epicode.CapstoneProjectBackend.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SongDto {
    @NotEmpty(message = "Il campo titolo non può essere vuoto")
    private String titolo;
    @NotEmpty(message = "Il campo artista non può essere vuoto")
    private String artista;
    @NotEmpty(message = "Il campo testo non può essere vuoto")
    private String testo;

    @NotEmpty(message = "Il campo lingua non può essere vuoto")
    private String lingua;
}
