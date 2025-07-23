package it.epicode.CapstoneProjectBackend.repository;

import it.epicode.CapstoneProjectBackend.model.Quiz;
import it.epicode.CapstoneProjectBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

    Optional<Quiz> findByUserAndDate(User user, LocalDate date);

    //Query per sbloccare un badge ogni 100 punti
    @Query("SELECT SUM(q.score) FROM Quiz q WHERE q.user.id = :userId")
    Integer sumPointsByUser(@Param("userId") Integer userId);
}
