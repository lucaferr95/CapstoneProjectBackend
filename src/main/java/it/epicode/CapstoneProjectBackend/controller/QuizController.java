package it.epicode.CapstoneProjectBackend.controller;

import it.epicode.CapstoneProjectBackend.Service.QuizService;
import it.epicode.CapstoneProjectBackend.Service.UserService;
import it.epicode.CapstoneProjectBackend.dto.QuizResultDTO;
import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuizService quizService;

    @PostMapping("/submit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> submitQuiz(@RequestBody QuizResultDTO result, @AuthenticationPrincipal UserDetails userDetails) throws NotFoundException {
        User user = userService.findByUsername(userDetails.getUsername());

        if (quizService.hasAlreadySubmitted(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Hai già completato il quiz oggi.");
        }

        quizService.saveQuiz(user, result.getScore());
        return ResponseEntity.ok("Quiz salvato con successo");
    }
    @GetMapping("/punti")
    public ResponseEntity<Integer> getUserPoints(@AuthenticationPrincipal UserDetails userDetails) throws NotFoundException {
        User user = userService.findByUsername(userDetails.getUsername());
        int punti = quizService.getPoints(user);
        System.out.println("Punti calcolati: " + punti);

        return ResponseEntity.ok(punti);

    }


}
