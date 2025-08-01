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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private UserService userService;

    // Endpoint pubblici GET
    private final String[] publicGetEndpoints = new String[]{
            "/api/lyrics/**",
            "/api/feedback",
            "/api/feedback/**"
    };

    // Endpoint completamente esclusi dal filtro
    private final String[] excludedEndpoints = new String[]{
            "/auth/login",
            "/auth/register",
            "/html/**",
            "/login.html",
            "/register.html",
            "/css/**",
            "/js/**",
            "/images/**",
            "/favicon.ico",
            "/uploads/avatars/**"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        AntPathMatcher matcher = new AntPathMatcher();

        return Arrays.stream(excludedEndpoints).anyMatch(e -> matcher.match(e, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String method = request.getMethod();
        AntPathMatcher matcher = new AntPathMatcher();

        // Consenti accesso libero agli endpoint GET pubblici
        if (method.equals("GET") && Arrays.stream(publicGetEndpoints).anyMatch(p -> matcher.match(p, path))) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token non presente o malformato");
        }

        String token = authorization.substring(7);

        try {
            jwtTool.validateToken(token);
            String username = jwtTool.getUsernameFromToken(token);

            UserDetails userDetails = userService.loadUserByUsername(username);
            List<String> roles = jwtTool.getRolesFromToken(token); // estrae ["ROLE_ADMIN"]

            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, authorities
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            System.out.println("✅ Token valido per utente: " + username);

        } catch (UsernameNotFoundException ex) {
            throw new UnauthorizedException("Utente collegato al token non trovato");
        } catch (Exception ex) {
            throw new UnauthorizedException("Token non valido");
        }

        filterChain.doFilter(request, response);
    }
}
