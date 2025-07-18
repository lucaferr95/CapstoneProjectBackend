package it.epicode.CapstoneProjectBackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LyricsDTO {

    @JsonProperty("lyrics")
    private String lyrics;

    public LyricsDTO() {}

    public LyricsDTO(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }
}
