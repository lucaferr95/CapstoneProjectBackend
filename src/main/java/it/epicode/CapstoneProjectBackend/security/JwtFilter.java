package it.epicode.CapstoneProjectBackend.security;

import it.epicode.CapstoneProjectBackend.Service.UserService;
import it.epicode.CapstoneProjectBackend.exception.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private UserService userService;

    private final String[] publicGetEndpoints = new String[]{
            "/api/lyrics/**",
            "/api/feedback",
            "/api/feedback/**"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String method = request.getMethod();
        AntPathMatcher matcher = new AntPathMatcher();

        // Consenti accesso libero a endpoint pubblici GET
        if (method.equals("GET") && Arrays.stream(publicGetEndpoints).anyMatch(p -> matcher.match(p, path))) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token non presente o malformato");
        }

        String token = authorization.substring(7); // ✅ DEVE essere fuori dal try per essere visibile

        try {
            jwtTool.validateToken(token);
            String username = jwtTool.getUsernameFromToken(token);
            UserDetails userDetails = userService.loadUserByUsername(username);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (UsernameNotFoundException ex) {
            throw new UnauthorizedException("Utente collegato al token non trovato");
        } catch (Exception ex) {
            throw new UnauthorizedException("Token non valido");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String[] excludedEndpoints = {
                "/auth/login",
                "/auth/register",
                "/html/**",
                "/login.html",
                "/register.html",
                "/css/**",
                "/js/**",
                "/images/**",
                "/favicon.ico",
                "/api/lyrics/**",
                "/uploads/avatars/**"
        };

        return Arrays.stream(excludedEndpoints)
                .anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
    }
}
