package it.epicode.CapstoneProjectBackend.Service;

import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.model.Points;
import it.epicode.CapstoneProjectBackend.repository.PointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PointsService {

    @Autowired
    private PointsRepository pointsRepository;

    public void updatePoints(int userId, int newPoints) {
        Points entry = pointsRepository.findByUserId(userId)
                .orElse(new Points());

        entry.setUserId(userId);
        entry.setLastUpdated(LocalDate.now());
        entry.setPoints(newPoints);


        pointsRepository.save(entry);
    }

    public Points getPoints(int userId) throws NotFoundException {
        return pointsRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Nessun dato punti trovato"));
    }
    public List<Points> getAllPoints() {
        return pointsRepository.findAll();
    }

}
