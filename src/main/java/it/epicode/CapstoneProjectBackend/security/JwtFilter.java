package it.epicode.CapstoneProjectBackend.security;

import it.epicode.CapstoneProjectBackend.Service.UserService;
import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.exception.UnauthorizedException;
import it.epicode.CapstoneProjectBackend.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        if (method.equals("GET") &&
                Arrays.stream(publicGetEndpoints).anyMatch(p -> matcher.match(p, path))) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token non presente");
        }

        String token = authorization.substring(7);
        jwtTool.validateToken(token);

        try {
            String username = jwtTool.getUsernameFromToken(token);
            User user = userService.findByUsername(username);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user, // Salva direttamente il tuo oggetto User
                    null,
                    user.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (NotFoundException e) {
            throw new UnauthorizedException("User collegato al token non trovato");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
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
