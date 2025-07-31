package it.epicode.CapstoneProjectBackend.Service;

import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.model.Points;
import it.epicode.CapstoneProjectBackend.model.User;
import it.epicode.CapstoneProjectBackend.repository.PointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PointsService {

    @Autowired
    private PointsRepository pointsRepository;

    // Aggiornamento manuale da ADMIN
    public void updatePoints(int userId, int newPoints) {
        Points entry = pointsRepository.findByUserId(userId).orElse(new Points());
        entry.setUserId(userId);
        entry.setLastUpdated(LocalDate.now());
        entry.setPoints(newPoints);
        pointsRepository.save(entry);
    }

    // Recupera i punti dalla tabella (usata per preferiti e punteggio totale)
    public Points getPoints(int userId) throws NotFoundException {
        return pointsRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Nessun dato punti trovato"));
    }

    // Classifica completa
    public List<Points> getAllPoints() {
        return pointsRepository.findAll();
    }

    // Aggiunta automatica (preferiti) con limite giornaliero di 20
    public void addPointsIfNotExceeded(int userId, int pointsToAdd) {
        Points entry = pointsRepository.findByUserId(userId).orElse(new Points());
        LocalDate today = LocalDate.now();

        if (!today.equals(entry.getLastUpdated())) {
            entry.setPoints(pointsToAdd);
            entry.setLastUpdated(today);
        } else if (entry.getPoints() < 20) {
            int nuoviPunti = Math.min(20, entry.getPoints() + pointsToAdd);
            entry.setPoints(nuoviPunti);
        } else {
            return; // limite già raggiunto
        }

        entry.setUserId(userId);
        pointsRepository.save(entry);
    }

    // Metodo usato da /punti/aggiungi (quiz o preferiti, nessun limite applicato)
    public void aggiungiPuntiUtente(User user, int amount) {
        Points punti = pointsRepository.findByUserId(user.getId())
                .orElseGet(() -> new Points(user, 0));
        punti.setPoints(punti.getPoints() + amount);
        pointsRepository.save(punti);
    }
}
