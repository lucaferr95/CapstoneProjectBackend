package it.epicode.CapstoneProjectBackend.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import it.epicode.CapstoneProjectBackend.dto.UserDto;
import it.epicode.CapstoneProjectBackend.dto.UserResponseDto;
import it.epicode.CapstoneProjectBackend.enums.UserType;
import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.exception.UnauthorizedException;
import it.epicode.CapstoneProjectBackend.model.User;
import it.epicode.CapstoneProjectBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSenderImpl javaMailSender;
    @Autowired
    private Cloudinary cloudinary;

    public UserResponseDto toResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setNome(user.getNome());
        response.setCognome(user.getCognome());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setUserType(user.getUserType());
        return response;
    }

    public User saveUser(UserDto userDto) {

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username già in uso");
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email già in uso");
        }

        User user = new User();
        user.setNome(userDto.getNome());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setCognome(userDto.getCognome());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUserType(UserType.USER);

        sendMail(user.getEmail(), user);
        return userRepository.save(user);
    }

    public User getUser(int id) throws NotFoundException {
        return userRepository.findById(id).
                orElseThrow(() -> new NotFoundException("User con id "
                        + id + " non trovato"));
    }

    public Page<User> getAllUsers(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    public User updateUser(int id, UserDto userDto)
            throws NotFoundException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userAutenticato = findByUsername(username);


        if (!userAutenticato.getUserType().name().equals("ADMIN") && userAutenticato.getId() != id) {
            throw new UnauthorizedException("Non puoi modificare un altro user.");
        }

        User userDaAggiornare = getUser(id);

        userDaAggiornare.setNome(userDto.getNome());
        userDaAggiornare.setEmail(userDto.getEmail());
        userDaAggiornare.setUsername(userDto.getUsername());
        userDaAggiornare.setCognome(userDto.getCognome());
        if (!passwordEncoder.matches(userDto.getPassword(), userDaAggiornare.getPassword())) {
            userDaAggiornare.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }


        return userRepository.save(userDaAggiornare);
    }

    public void deleteUser(int id) throws NotFoundException {
        User userAutenticato = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userAutenticato.getUserType().name().equals("ADMIN") && userAutenticato.getId() != id) {
            throw new UnauthorizedException("Non puoi eliminare un altro user.");
        }

        User userDaEliminare = getUser(id);

        userRepository.delete(userDaEliminare);
    }

    private void sendMail(String email, User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Registrazione al nostro sito");
        message.setText("Ciao " + user.getNome() + "!\n\n" +
                "Benvenuto/a su Fuori di Testo. Siamo felici che tu sia dei nostri 🎶\n\n" +
                "Username: " + user.getUsername() + "\n\n" +
                "Buon divertimento!");

        javaMailSender.send(message);
    }

    public User findByUsername(String username) throws NotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utente non trovato con username: " + username));
    }

    public String patchUser(String username, MultipartFile file) throws IOException, NotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String avatarUrl = (String) uploadResult.get("secure_url");

        user.setAvatar(avatarUrl);
        userRepository.save(user);

        return avatarUrl;
    }




}
