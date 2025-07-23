package it.epicode.CapstoneProjectBackend.repository;


import it.epicode.CapstoneProjectBackend.model.Points;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointsRepository extends JpaRepository<Points, Integer> {
    Optional<Points> findByUserId(int userId);
}
