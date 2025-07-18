package it.epicode.CapstoneProjectBackend.repository;

import it.epicode.CapstoneProjectBackend.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository <Feedback, Integer> {

}
