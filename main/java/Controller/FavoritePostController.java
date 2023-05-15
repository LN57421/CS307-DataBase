package Controller;


import EmbededClass.FavoritePostsId;
import TableClass.FavoritePost;
import ServiceClass.FavoritePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/favoritePosts")
public class FavoritePostController {
    private final FavoritePostService favoritePostService;

    @Autowired
    public FavoritePostController(FavoritePostService favoritePostService) {
        this.favoritePostService = favoritePostService;
    }

    @PostMapping("/favorite")
    public ResponseEntity<?> addFavoritePost(@Valid @RequestBody FavoritePost favoritePost) {
        FavoritePost addedFavoritePost = favoritePostService.addFavoritePost(favoritePost);
        return new ResponseEntity<>(addedFavoritePost, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/favoritePosts")
    public ResponseEntity<?> getFavoritePostsByUserId(@PathVariable Long userId) {
        List<FavoritePost> favoritePosts = favoritePostService.getFavoritePostsByUserId(userId);
        if (favoritePosts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("该用户没有进行收藏或者找不到该用户" + userId);
        } else {
            return ResponseEntity.ok(favoritePosts);
        }
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

