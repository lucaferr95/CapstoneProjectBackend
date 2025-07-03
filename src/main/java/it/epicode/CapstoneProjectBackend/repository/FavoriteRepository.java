package it.epicode.CapstoneProjectBackend.repository;

import it.epicode.CapstoneProjectBackend.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository <Favorite, Integer> {

    //trovare i testi preferiti di un utente
    List<Favorite> findByUserId(int userId);

    //verifico se un brano è già tra i preferiti
    Optional<Favorite> findByUserIdAndSongId(int userId, int songId);

    //rimuovere un preferito specifico
    void deleteByUserIdAndSongId(int userId, int songId);

}
