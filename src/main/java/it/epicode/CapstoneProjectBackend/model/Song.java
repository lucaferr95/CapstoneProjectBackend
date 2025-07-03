package it.epicode.CapstoneProjectBackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue
    private int id;
    private String titolo;
    private String artista;
    @Lob
    @Column(nullable = false)
    private String testo;
    private String lingua;
}
