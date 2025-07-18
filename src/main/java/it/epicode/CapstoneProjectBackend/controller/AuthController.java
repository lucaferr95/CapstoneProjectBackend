package it.epicode.CapstoneProjectBackend.controller;

import it.epicode.CapstoneProjectBackend.Service.AuthService;
import it.epicode.CapstoneProjectBackend.Service.UserService;
import it.epicode.CapstoneProjectBackend.dto.LoginDto;
import it.epicode.CapstoneProjectBackend.dto.UserDto;
import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.exception.ValidationException;
import it.epicode.CapstoneProjectBackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/auth/register")
    public User register(@RequestBody @Validated UserDto userDto,
                         BindingResult bindingResult) throws ValidationException {

        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().
                    stream().map(objectError -> objectError.getDefaultMessage())
                    .reduce("",(e,s)->e+s));
        }
        return userService.saveUser(userDto);
    }

    @CrossOrigin(origins = "http://localhost:63342/")
    @PostMapping("/auth/login")
    public String login(@RequestBody @Validated LoginDto loginDto,
                        BindingResult bindingResult) throws ValidationException, NotFoundException {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().
                    stream().map(objectError -> objectError.getDefaultMessage())
                    .reduce("",(e,s)->e+s));
        }


        return authService.login(loginDto);
    }
    @PostMapping("/auth/logout")
    public String logout(@RequestBody @Validated LoginDto loginDto,
                        BindingResult bindingResult) throws ValidationException, NotFoundException {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().
                    stream().map(objectError -> objectError.getDefaultMessage())
                    .reduce("",(e,s)->e+s));
        }


        return authService.login(loginDto);
    }
    @GetMapping("/auth/me")
    public User getLoggedUser(Authentication authentication) throws NotFoundException {
        return userService.findByUsername(authentication.getName());
    }

}
