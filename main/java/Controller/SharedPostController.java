package Controller;

import EmbededClass.SharedPostsId;
import TableClass.SharedPost;
import ServiceClass.SharedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/sharedPosts")
public class SharedPostController {
    private final SharedPostService sharedPostService;

    @Autowired
    public SharedPostController(SharedPostService sharedPostService) {
        this.sharedPostService = sharedPostService;
    }


    @PostMapping("/share")
    public ResponseEntity<?> addSharedPost(@RequestBody SharedPost sharedPost) {
        SharedPost addSharedPost = sharedPostService.addSharedPost(sharedPost);
        return new ResponseEntity<>(addSharedPost, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/sharedPosts")
    public ResponseEntity<?> getSharedPostsByUserId(@PathVariable Long userId) {
        List<SharedPost> sharedPosts = sharedPostService.getSharedPostsByUserId(userId);
        if (sharedPosts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No shared posts found for user with ID: " + userId);
        } else {
            return ResponseEntity.ok(sharedPosts);
        }
    }

    @GetMapping
    public List<SharedPost> findAllSharedPosts() {
        return sharedPostService.findAllSharedPosts();
    }

    @PutMapping
    public SharedPost updateSharedPost(@RequestBody SharedPost sharedPost) {
        return sharedPostService.updateSharedPost(sharedPost);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteSharedPost(@PathVariable SharedPostsId id) {
        sharedPostService.deleteSharedPost(id);
    }
}

