package it.epicode.CapstoneProjectBackend.dto;
//Non restituisce i dati sensibili dell'utente


import it.epicode.CapstoneProjectBackend.enums.UserType;
import lombok.Data;

@Data
public class UserResponseDto {
    private int id;
    private String nome;
    private String cognome;
    private String username;
    private String email;
    private UserType userType;
}
