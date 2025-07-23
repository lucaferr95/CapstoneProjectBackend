package it.epicode.CapstoneProjectBackend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
public class Feedback {
    @Id
    @GeneratedValue
    private int id;
    private String comment;
    @CreationTimestamp
    private LocalDate createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private String type = "feedback";

}
