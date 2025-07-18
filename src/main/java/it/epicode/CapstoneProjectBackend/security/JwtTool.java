package it.epicode.CapstoneProjectBackend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.epicode.CapstoneProjectBackend.Service.UserService;
import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTool {

    @Value("${jwt.duration}")
    private Long durata;
    @Value("${jwt.secret}")
    private String chiaveSegreta;

    @Autowired
    private UserService userService;

    public String createToken(User user) {
        // Estraggo le authorities come lista di stringhe
        List<String> authorities = user.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .toList();

        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + durata))
                .subject(user.getId() + "")
                .claim("username", user.getUsername())
                .claim("role", user.getUserType().name()) // opzionale se tieni anche 'authorities'
                .claim("authorities", authorities)        // ✅ aggiunto
                .signWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes()))
                .compact();
    }



    public void validateToken(String token){
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes()))
                .build().parse(token);
    }

    public User getUserFromToken(String token) throws NotFoundException {
        int id = Integer.parseInt(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes()))
                .build().parseSignedClaims(token).getPayload().getSubject());

        return userService.getUser(id);
    }
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

}
