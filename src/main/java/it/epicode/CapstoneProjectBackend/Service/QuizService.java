package it.epicode.CapstoneProjectBackend.Service;

import it.epicode.CapstoneProjectBackend.model.Quiz;
import it.epicode.CapstoneProjectBackend.model.User;
import it.epicode.CapstoneProjectBackend.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public Quiz saveQuiz(User user, int score) {
        int validScore = Math.min(score, 20); // 4 domande max x 5 punti
        Quiz quiz = new Quiz();
        quiz.setUser(user);
        quiz.setDate(LocalDate.now());
        quiz.setScore(validScore);
        return quizRepository.save(quiz);
    }


    public boolean hasAlreadySubmitted(User user) {
        return quizRepository.findByUserAndDate(user, LocalDate.now()).isPresent();
    }

    public Integer getTotalScore(User user) {
        return quizRepository.getTotalScore(user);
    }
}
