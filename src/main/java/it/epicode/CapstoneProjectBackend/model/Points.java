package it.epicode.CapstoneProjectBackend.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.time.LocalDate;

@Entity
@Data
public class Points {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private int userId;
    @Column(nullable = false)
    private LocalDate lastUpdated;
    @Column(nullable = false)
    private int points;
    private int dailyActionsCount;

    @ManyToOne
    private User user;

    public Points(User user, int points) {
        this.user = user;
        this.userId = user.getId(); // se hai anche questo campo separato
        this.points = points;
        this.dailyActionsCount = 0;
        this.lastUpdated = LocalDate.now();
    }
    public Points() {
    }
}
