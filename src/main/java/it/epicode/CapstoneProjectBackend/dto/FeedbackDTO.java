package it.epicode.CapstoneProjectBackend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class FeedbackDTO {
    @NotEmpty(message = "Il campo 'comment' non può essere vuoto")
    private String comment;
}
