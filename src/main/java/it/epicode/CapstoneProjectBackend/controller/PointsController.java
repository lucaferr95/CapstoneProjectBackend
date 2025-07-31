package it.epicode.CapstoneProjectBackend.controller;

import it.epicode.CapstoneProjectBackend.Service.PointsService;
import it.epicode.CapstoneProjectBackend.Service.QuizService;
import it.epicode.CapstoneProjectBackend.Service.UserService;
import it.epicode.CapstoneProjectBackend.dto.PointsDTO;
import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.model.Points;
import it.epicode.CapstoneProjectBackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/punti")
public class PointsController {

    @Autowired
    private PointsService pointsService;
    @Autowired
    private UserService userService;
    @Autowired
    private QuizService quizService;
    @PostMapping("/aggiungi")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> aggiungiPunti(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestParam int amount) throws NotFoundException {
        User user = userService.findByUsername(userDetails.getUsername());
        pointsService.aggiungiPuntiUtente(user, amount);
        return ResponseEntity.ok("Punti aggiunti con successo");
    }

    @PostMapping("/manuale")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateManualPoints(@RequestBody PointsDTO dto) {
        pointsService.updatePoints(dto.getUserId(), dto.getNewPoints());
        return ResponseEntity.ok("Punti aggiornati con successo");
    }

    @GetMapping("/manuale/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getManualPoints(@PathVariable int userId) throws NotFoundException {
        return ResponseEntity.ok(pointsService.getPoints(userId));
    }
    @GetMapping("/classifica")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Points>> getAllPoints() {
        List<Points> punti = pointsService.getAllPoints();
        return ResponseEntity.ok(punti);
    }
    @GetMapping("/totali")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Integer> getTotalPoints(@AuthenticationPrincipal UserDetails userDetails) throws NotFoundException {
        User user = userService.findByUsername(userDetails.getUsername());

        int quizPoints = quizService.getPoints(user); // somma quiz
        int manualPoints = pointsService.getPoints(user.getId()).getPoints(); // da tabella manuale

        int total = quizPoints + manualPoints;

        return ResponseEntity.ok(total);
    }

}
