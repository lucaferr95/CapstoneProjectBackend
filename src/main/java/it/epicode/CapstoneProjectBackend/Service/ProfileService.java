package it.epicode.CapstoneProjectBackend.Service;

import it.epicode.CapstoneProjectBackend.model.Profile;
import it.epicode.CapstoneProjectBackend.model.User;
import it.epicode.CapstoneProjectBackend.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public Profile getProfile(User user) {
        return profileRepository.findByUser(user)
                .orElse(new Profile()); // se il profilo non esiste, ne crea uno vuoto
    }

    public Profile updateProfile(User user, Profile updatedProfile) {
        Profile profile = profileRepository.findByUser(user)
                .orElse(new Profile());

        profile.setArtist(updatedProfile.getArtist());
        profile.setQuote(updatedProfile.getQuote());
        profile.setAlbum(updatedProfile.getAlbum());
        profile.setTracks(updatedProfile.getTracks());
        profile.setUser(user);

        return profileRepository.save(profile);
    }
}

