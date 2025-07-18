package it.epicode.CapstoneProjectBackend.controller;

import it.epicode.CapstoneProjectBackend.Service.ProfileService;
import it.epicode.CapstoneProjectBackend.Service.UserService;
import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.model.Profile;
import it.epicode.CapstoneProjectBackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public Profile getMyProfile(Authentication authentication) throws NotFoundException {
        User user = userService.findByUsername(authentication.getName());
        return profileService.getProfile(user);
    }

    @PutMapping("/me")
    public Profile updateMyProfile(@RequestBody Profile updatedProfile,
                                   Authentication authentication) throws NotFoundException {
        User user = userService.findByUsername(authentication.getName());
        return profileService.updateProfile(user, updatedProfile);
    }
}
