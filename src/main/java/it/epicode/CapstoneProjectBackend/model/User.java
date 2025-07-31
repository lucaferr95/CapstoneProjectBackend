package it.epicode.CapstoneProjectBackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.CapstoneProjectBackend.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;
    @Size(min = 4, max = 255)
    private String username;
    @Column(unique = true)
    private String email;
    @Size(min = 4, max = 255)
    private String password;
    private String nome;
    private String cognome;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //se cancello un utente si cancellano anche i preferiti
    @JsonIgnore
    private List<Favorite> favorites;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Feedback> feedback;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Points> points;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = userType != null ? "ROLE_" + userType.name().toUpperCase() : "ROLE_USER";
        return List.of(new SimpleGrantedAuthority(role));
    }



    @Override
    public boolean isAccountNonExpired() {
        return true;

    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", avatar='" + avatar + '\'' +
                ", userType=" + userType +
                '}';
    }



}