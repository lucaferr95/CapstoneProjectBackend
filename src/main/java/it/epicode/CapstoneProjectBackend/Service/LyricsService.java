package it.epicode.CapstoneProjectBackend.Service;

import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LyricsService {

    private final RestTemplate restTemplate;

    @Value("${rapidapi.key}")
    private String rapidApiKey;

    private static final String RAPIDAPI_HOST = "genius-song-lyrics1.p.rapidapi.com";

    public LyricsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getLyricsText(String artist, String title) {
        String query = title + " " + artist;
        String searchUrl = "https://" + RAPIDAPI_HOST + "/search/?q=" + query;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", rapidApiKey);
        headers.set("X-RapidAPI-Host", RAPIDAPI_HOST);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            // 🔍 1. Cerca la canzone
            ResponseEntity<JsonNode> searchResponse = restTemplate.exchange(
                    searchUrl, HttpMethod.GET, requestEntity, JsonNode.class
            );

            JsonNode hits = searchResponse.getBody().path("hits");
            if (!hits.isArray() || hits.size() == 0) {
                return "Testo non trovato";
            }

            // 🎯 2. Prendi l'ID della canzone
            String songId = hits.get(0).path("result").path("id").asText();
            String lyricsUrl = "https://" + RAPIDAPI_HOST + "/song/lyrics/?id=" + songId;


            // 📄 3. Recupera il testo HTML
            ResponseEntity<JsonNode> lyricsResponse = restTemplate.exchange(
                    lyricsUrl, HttpMethod.GET, requestEntity, JsonNode.class
            );

            JsonNode htmlNode = lyricsResponse.getBody()
                    .path("lyrics")
                    .path("lyrics")
                    .path("body")
                    .path("html");

            if (htmlNode.isMissingNode()) {
                return "Testo non disponibile";
            }

            // 🧼 4. Rimuove i tag HTML
            String plainText = Jsoup.parse(htmlNode.asText()).text();
            return plainText;

        } catch (Exception e) {
            e.printStackTrace();
            return "Errore durante il recupero del testo";
        }
    }
}
