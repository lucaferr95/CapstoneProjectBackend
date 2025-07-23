package it.epicode.CapstoneProjectBackend.Service;

import it.epicode.CapstoneProjectBackend.dto.FeedbackDTO;
import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.exception.UnauthorizedException;
import it.epicode.CapstoneProjectBackend.model.Feedback;
import it.epicode.CapstoneProjectBackend.model.User;
import it.epicode.CapstoneProjectBackend.repository.FeedbackRepository;
import it.epicode.CapstoneProjectBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    public Feedback saveFeedback(FeedbackDTO feedbackDTO, String username) throws NotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        Feedback feedback = new Feedback();
        feedback.setComment(feedbackDTO.getComment());
        feedback.setType(feedbackDTO.getType());
        feedback.setUser(user);

        return feedbackRepository.save(feedback);
    }

    public Feedback getFeedback(int id) throws NotFoundException {
        return feedbackRepository.findById(id).orElseThrow(() -> new NotFoundException("Feedback con id: " + id + " non trovato"));

    }

    public List<Feedback> getAllFeedbackForAdmin() {
        return feedbackRepository.findAll();
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    public void deleteFeedback(int id) throws NotFoundException {
        Feedback feedbackDaRimuovere = getFeedback(id);
        feedbackRepository.delete(feedbackDaRimuovere);
    }

    public void deleteFeedbackAndAuthor(int feedbackId) throws NotFoundException {
        Feedback fb = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new NotFoundException("Feedback non trovato"));

        User user = fb.getUser();

        feedbackRepository.deleteById(feedbackId); // ✅ prima cancella il feedback

        if (user != null && user.getId() != null) {
            userRepository.deleteById(user.getId()); // ✅ poi elimina l'utente solo se persistito
        }
    }
}
