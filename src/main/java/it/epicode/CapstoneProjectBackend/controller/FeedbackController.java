package it.epicode.CapstoneProjectBackend.controller;

import it.epicode.CapstoneProjectBackend.Service.FeedbackService;
import it.epicode.CapstoneProjectBackend.dto.FeedbackDTO;
import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.model.Feedback;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public Feedback submitFeedback(@RequestBody @Valid FeedbackDTO feedbackDTO,
                                   @AuthenticationPrincipal UserDetails userDetails) throws NotFoundException {
        return feedbackService.saveFeedback(feedbackDTO, userDetails.getUsername());
    }

    @GetMapping
    public List<Feedback> getAllFeedback() {
        return feedbackService.getAllFeedback();
    }
    @GetMapping("/debug-auth")
    public ResponseEntity<?> debugAuth(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userDetails.getAuthorities());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteFeedback(@PathVariable int id) throws NotFoundException {
        feedbackService.deleteFeedback(id);
    }

    // Backoffice — solo admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/backoffice")
    public ResponseEntity<List<Feedback>> getAllFeedbackForAdmin() {
        return ResponseEntity.ok(feedbackService.getAllFeedbackForAdmin());
    }
    @DeleteMapping("/{id}/with-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFeedbackAndUser(@PathVariable int id) throws NotFoundException {
        feedbackService.deleteFeedbackAndAuthor(id);
        return ResponseEntity.noContent().build();
    }

}
