package it.epicode.CapstoneProjectBackend.security;


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
    // vai su errore, implement method

    @Autowired
    private JwtTool jwtTool;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if(authorization== null || !authorization.startsWith("Bearer ")){
            throw new UnauthorizedException("Token non presente");
        } else {
            String token = authorization.substring(7); // mi prendo la parte dopo "bearer "
            jwtTool.validateToken(token);

            try{


                User user = jwtTool.getUserFromToken(token);

                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());


                SecurityContextHolder.getContext().setAuthentication(authentication);


            } catch (NotFoundException e) {
                throw new UnauthorizedException("User collegato al token non trovato");
            }




            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludedEndpoints = new String[] {
                "/auth/**",
                "/html/**",
                "/login.html",
                "/register.html",
                "/css/**",
                "/js/**",
                "/images/**",
                "/favicon.ico"
        };

        return Arrays.stream(excludedEndpoints)
                .anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
    }

}
