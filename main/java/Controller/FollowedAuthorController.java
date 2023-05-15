package Controller;

import EmbededClass.FollowedAuthorsId;
import ServiceClass.FollowedAuthorService;
import TableClass.FollowedAuthor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/followedAuthors")
public class FollowedAuthorController {

    private final FollowedAuthorService followedAuthorService;

    @Autowired
    public FollowedAuthorController(FollowedAuthorService followedAuthorService) {
        this.followedAuthorService = followedAuthorService;
    }

    @PostMapping("/follow")
    public ResponseEntity<String> followAuthor(@RequestBody FollowedAuthor followedAuthor) {
        String newFollowedAuthor = followedAuthorService.followAuthor(followedAuthor);
        return new ResponseEntity<>(newFollowedAuthor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FollowedAuthor>> findAllFollowedAuthors() {
        List<FollowedAuthor> followedAuthors = followedAuthorService.findAllFollowedAuthors();
        return new ResponseEntity<>(followedAuthors, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<FollowedAuthor> updateFollowedAuthor(@RequestBody FollowedAuthor followedAuthor) {
        FollowedAuthor updatedFollowedAuthor = followedAuthorService.updateFollowedAuthor(followedAuthor);
        return new ResponseEntity<>(updatedFollowedAuthor, HttpStatus.OK);
    }

    @DeleteMapping("/{authorId}/{followerAuthorId}")
    public ResponseEntity<?> unfollowAuthor(@PathVariable String authorId, @PathVariable String followerAuthorId) {
        FollowedAuthorsId id = new FollowedAuthorsId(authorId, followerAuthorId);
        String result = followedAuthorService.unfollowAuthor(id);
        return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users/{userId}/findFollowedPosts")
    public ResponseEntity<?> findFollowedByAuthor(@PathVariable String userId) {
        List<FollowedAuthor> followAuthors = followedAuthorService.findFollowedByAuthor(userId);
        if (followAuthors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("关注者为空或者找不到对应id" + userId);
        } else {
            return ResponseEntity.ok(followAuthors);
        }
    }
}
