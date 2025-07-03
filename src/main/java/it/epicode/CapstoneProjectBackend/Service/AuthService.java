package it.epicode.CapstoneProjectBackend.Service;

import it.epicode.CapstoneProjectBackend.dto.LoginDto;
import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.model.User;

import it.epicode.CapstoneProjectBackend.repository.UserRepository;
import it.epicode.CapstoneProjectBackend.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String login(LoginDto loginDto) throws NotFoundException {
        User user = userRepository.findByUsername(loginDto.getUsername()).orElseThrow(
                ()->new NotFoundException("Utente con username/password non trovato"));
        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            return jwtTool.createToken(user);
        }else {
            throw new NotFoundException("Utente con username/password non trovato");
        }


    }
}
