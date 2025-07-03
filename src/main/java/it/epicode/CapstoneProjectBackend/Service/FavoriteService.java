package it.epicode.CapstoneProjectBackend.Service;

import it.epicode.CapstoneProjectBackend.dto.FavoriteDto;
import it.epicode.CapstoneProjectBackend.exception.AlreadyFavException;
import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.model.Favorite;
import it.epicode.CapstoneProjectBackend.model.Song;
import it.epicode.CapstoneProjectBackend.model.User;
import it.epicode.CapstoneProjectBackend.repository.FavoriteRepository;
import it.epicode.CapstoneProjectBackend.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private SongRepository songRepository;

    public Favorite addFavorite(User user, FavoriteDto dto) throws NotFoundException {
        Song song = songRepository.findById(dto.getSongId())
                .orElseThrow(() -> new NotFoundException("Canzone non trovata"));

        // Evita duplicati
        if (favoriteRepository.findByUserIdAndSongId(user.getId(), dto.getSongId()).isPresent()) {
            throw new AlreadyFavException("La canzone è già nei preferiti");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setSong(song);
        favorite.setSavedAt(LocalDateTime.now());

        return favoriteRepository.save(favorite);
    }

    public List<Favorite> getFavoritesByUser(User user) {
        return favoriteRepository.findByUserId(user.getId());
    }

    public void removeFavorite(User user, int songId) throws NotFoundException {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new NotFoundException("Canzone non trovata"));

        Favorite favorite = favoriteRepository.findByUserIdAndSongId(user.getId(), songId)
                .orElseThrow(() -> new NotFoundException("Canzone non trovata nei preferiti"));

        favoriteRepository.delete(favorite);
    }
}
