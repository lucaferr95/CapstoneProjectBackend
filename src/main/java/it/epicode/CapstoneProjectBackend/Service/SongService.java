package it.epicode.CapstoneProjectBackend.Service;


import it.epicode.CapstoneProjectBackend.dto.SongDto;
import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.model.Song;
import it.epicode.CapstoneProjectBackend.repository.FavoriteRepository;
import it.epicode.CapstoneProjectBackend.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    public Song saveSong(SongDto songDto){
        Song song = new Song();
        song.setTitolo(songDto.getTitolo());
        song.setArtista(songDto.getArtista());
        song.setLingua(songDto.getLingua());
        song.setTesto(songDto.getTesto());
        return songRepository.save(song);
    }

    public Song getSong(int id) throws NotFoundException {
        return songRepository.findById(id).
                orElseThrow(()-> new NotFoundException("Brano con id "
                        + id + " non trovato"));
    }

    public Page<Song> getAllSongs(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return songRepository.findAll(pageable);
    }

    public Song updateSong(int id, SongDto songDto)
            throws NotFoundException {

        Song songDaAggiornare = getSong(id);

        songDaAggiornare.setTitolo(songDto.getTitolo());
        songDaAggiornare.setArtista(songDto.getArtista());
        songDaAggiornare.setTesto(songDto.getTesto());
        songDaAggiornare.setLingua(songDto.getLingua());

        return songRepository.save(songDaAggiornare);
    }
    public void deleteSong(int id) throws NotFoundException {

        Song songDaEliminare = getSong(id);

        songRepository.delete(songDaEliminare);
    }

    //ricerca di un brano per titolo ignorando i caratteri in minuscolo/maiuscolo
    public List<Song> searchByTitolo(String titolo) {
        return songRepository.findByTitoloContainingIgnoreCase(titolo);
    }

    //ricerca di un brano per artista ignorando i caratteri in minuscolo/maiuscolo
    public List<Song> searchByArtista(String artista) {
        return songRepository.findByArtistaContainingIgnoreCase(artista);
    }
}
