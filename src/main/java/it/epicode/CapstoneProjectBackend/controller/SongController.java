package it.epicode.CapstoneProjectBackend.controller;

import it.epicode.CapstoneProjectBackend.Service.SongService;
import it.epicode.CapstoneProjectBackend.dto.SongDto;
import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.model.Song;
import it.epicode.CapstoneProjectBackend.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/songs")
public class SongController {

        @Autowired
        private SongService songService;

        @PostMapping("")

        //possiamo aggiungere brani solo se siamo admin
        @PreAuthorize("hasAuthority('ADMIN')")
        @ResponseStatus(HttpStatus.CREATED)
        public Song createSong(@RequestBody @Validated SongDto songDto, BindingResult bindingResult)
                throws ValidationException {
            if (bindingResult.hasErrors()) {
                throw new ValidationException(bindingResult.getAllErrors().stream()
                        .map(objectError -> objectError.getDefaultMessage())
                        .collect(Collectors.joining("; ")));
            }
            return songService.saveSong(songDto);
        }

        @GetMapping("")
        public Page<Song> getAllSongs(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy) {
            return songService.getAllSongs(page, size, sortBy);
        }

        @GetMapping("/{id}")
        public Song getSong(@PathVariable int id) throws NotFoundException {
            return songService.getSong(id);
        }

        @PutMapping("/{id}")
        //posso aggiornare un brano solo se sono un admin
        @PreAuthorize("hasAuthority('ADMIN')")
        public Song updateSong(@PathVariable int id,
                               @RequestBody @Validated SongDto songDto,
                               BindingResult bindingResult) throws ValidationException, NotFoundException {
            if (bindingResult.hasErrors()) {
                throw new ValidationException(bindingResult.getAllErrors().stream()
                        .map(objectError -> objectError.getDefaultMessage())
                        .collect(Collectors.joining("; ")));
            }
            return songService.updateSong(id, songDto);
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasAuthority('ADMIN')")
        public void deleteSong(@PathVariable int id) throws NotFoundException {
            songService.deleteSong(id);
        }

        @GetMapping("/search/title")
        public List<Song> searchSongs(@RequestParam String title) {
            return songService.searchByTitolo(title);
        }

        @GetMapping("/search/artist")
        public List<Song> searchByArtista(@RequestParam String artista) {
            return songService.searchByArtista(artista);
        }
    }


