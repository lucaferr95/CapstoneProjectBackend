package it.epicode.CapstoneProjectBackend.repository;

import it.epicode.CapstoneProjectBackend.model.Profile;
import it.epicode.CapstoneProjectBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Optional<Profile> findByUser(User user);
}

