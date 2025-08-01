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

    @Autowired
    private UserService userService;

    // Aggiornamento manuale da ADMIN
    public void updatePoints(int userId, int newPoints) throws NotFoundException {
        User user = userService.findById(userId); // Ottiene l'entità utente
        Points entry = pointsRepository.findByUserId(userId)
                .orElse(new Points(user, 0)); // Crea nuovo record se assente

        entry.setPoints(newPoints);
        entry.setLastUpdated(LocalDate.now());
        pointsRepository.save(entry);
    }

    // Recupera i punti di un utente
    public Points getPoints(int userId) throws NotFoundException {
        return pointsRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Nessun dato punti trovato per l'utente con ID " + userId));
    }

    // Ritorna la classifica completa
    public List<Points> getAllPoints() {
        return pointsRepository.findAll();
    }

    // Aggiunta automatica con limite giornaliero di 20 punti
    public void addPointsIfNotExceeded(int userId, int pointsToAdd) throws NotFoundException {
        User user = userService.findById(userId);
        Points entry = pointsRepository.findByUserId(userId)
                .orElse(new Points(user, 0));

        LocalDate today = LocalDate.now();

        if (!today.equals(entry.getLastUpdated())) {
            // Nuovo giorno: reset contatore
            entry.setPoints(pointsToAdd);
            entry.setDailyActionsCount(1);
            entry.setLastUpdated(today);
        } else if (entry.getDailyActionsCount() < 4) {
            // Aggiunta consentita
            entry.setPoints(entry.getPoints() + pointsToAdd);
            entry.setDailyActionsCount(entry.getDailyActionsCount() + 1);
        } else {
            // Limite raggiunto
            return;
        }

        pointsRepository.save(entry);
    }


    // Aggiunta libera di punti (es. quiz)
    public void aggiungiPuntiUtente(User user, int amount) {
        Points punti = pointsRepository.findByUserId(user.getId())
                .orElseGet(() -> new Points(user, 0));
        punti.setPoints(punti.getPoints() + amount);
        punti.setLastUpdated(LocalDate.now());
        pointsRepository.save(punti);
    }
}
