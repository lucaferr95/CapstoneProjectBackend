package it.epicode.CapstoneProjectBackend.repository;

import it.epicode.CapstoneProjectBackend.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository <Song, Integer> {
    List<Song> findByTitoloContainingIgnoreCase(String titolo);
    List<Song> findByArtistaContainingIgnoreCase(String artista);


}
