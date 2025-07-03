package it.epicode.CapstoneProjectBackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {
    @NotEmpty(message = "Il campo nome non può essere vuoto")
    private String nome;
    @NotEmpty(message = "Il campo cognome non può essere vuoto")
    private String cognome;
    @Email(message = "L'email deve avere un formato valido, es: indirizzo@gmail.com")
    private String email;
    @NotEmpty(message = "Il campo username non può essere vuoto")
    private String username;
    @NotEmpty(message = "Il campo password non può essere vuoto")
    private String password;
}
