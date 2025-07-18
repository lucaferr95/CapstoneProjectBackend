package it.epicode.CapstoneProjectBackend.controller;

import it.epicode.CapstoneProjectBackend.Service.LyricsService;
import it.epicode.CapstoneProjectBackend.dto.LyricsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lyrics")
public class LyricsController {

    private final LyricsService lyricsService;

    public LyricsController(LyricsService lyricsService) {
        this.lyricsService = lyricsService;
    }

    @GetMapping
    public ResponseEntity<LyricsDTO> getLyrics(
            @RequestParam String artist,
            @RequestParam String title
    ) {
        String cleanLyrics = lyricsService.getLyricsText(artist, title);
        LyricsDTO dto = new LyricsDTO(cleanLyrics);
        return ResponseEntity.ok(dto);
    }
}
