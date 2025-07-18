package it.epicode.CapstoneProjectBackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue
    private int id;

    private String artist;
    private String quote;
    private String album;
    private String tracks;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


}

