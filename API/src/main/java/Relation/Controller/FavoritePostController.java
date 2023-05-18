package Relation.Controller;


import Relation.EmbededClass.FavoritePostsId;
import Relation.RelationClass.FavoritePost;
import Relation.ServiceClass.FavoritePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/favoritePosts")
public class FavoritePostController {
    private final FavoritePostService favoritePostService;

    @Autowired
    public FavoritePostController(FavoritePostService favoritePostService) {
        this.favoritePostService = favoritePostService;
    }

    @PostMapping
    public FavoritePost addFavoritePost(@RequestBody FavoritePost favoritePost) {
        return favoritePostService.addFavoritePost(favoritePost);
    }

    @GetMapping
    public List<FavoritePost> findAllFavoritePosts() {
        return favoritePostService.findAllFavoritePosts();
    }

    @PutMapping
    public FavoritePost updateFavoritePost(@RequestBody FavoritePost favoritePost) {
        return favoritePostService.updateFavoritePost(favoritePost);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteFavoritePost(@PathVariable FavoritePostsId id) {
        favoritePostService.deleteFavoritePost(id);
    }
}

