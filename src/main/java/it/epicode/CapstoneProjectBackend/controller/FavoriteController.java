package it.epicode.CapstoneProjectBackend.controller;

import it.epicode.CapstoneProjectBackend.Service.FavoriteService;
import it.epicode.CapstoneProjectBackend.dto.FavoriteDto;
import it.epicode.CapstoneProjectBackend.exception.NotFoundException;
import it.epicode.CapstoneProjectBackend.model.Favorite;
import it.epicode.CapstoneProjectBackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping
    public Favorite addFavorite(@RequestBody FavoriteDto dto,
                                @AuthenticationPrincipal User user) throws NotFoundException {
        return favoriteService.addFavorite(user, dto);
    }

    @GetMapping
    public List<Favorite> getMyFavorites(@AuthenticationPrincipal User user) {
        return favoriteService.getFavoritesByUser(user);
    }

    @DeleteMapping("/{songId}")
    public void deleteFavorite(@PathVariable int songId,
                               @AuthenticationPrincipal User user) throws NotFoundException{
        favoriteService.removeFavorite(user, songId);
    }
}
