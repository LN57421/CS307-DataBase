package Controller;

import EmbededClass.LikedPostsId;
import TableClass.FavoritePost;
import TableClass.LikedPost;
import ServiceClass.LikedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/likedPosts")
public class LikedPostController {
    private final LikedPostService likedPostService;

    @Autowired
    public LikedPostController(LikedPostService likedPostService) {
        this.likedPostService = likedPostService;
    }


    @PostMapping("/liked")
    public ResponseEntity<?> addLikedPost(@Valid @RequestBody LikedPost likedPost) {
        LikedPost addLikedPost = likedPostService.addLikedPost(likedPost);
        return new ResponseEntity<>(addLikedPost, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/likedPosts")
    public ResponseEntity<?> getLikedPostsByUserId(@PathVariable String userId) {
        List<LikedPost> likedPosts = likedPostService.getLikedPostsByUserId(userId);
        if (likedPosts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No liked posts found for user with ID: " + userId);
        } else {
            return ResponseEntity.ok(likedPosts);
        }
    }
    @GetMapping
    public List<LikedPost> findAllLikedPosts() {
        return likedPostService.findAllLikedPosts();
    }

    @PutMapping
    public LikedPost updateLikedPost(@RequestBody LikedPost likedPost) {
        return likedPostService.updateLikedPost(likedPost);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteLikedPost(@PathVariable LikedPostsId id) {
        likedPostService.deleteLikedPost(id);
    }
}
