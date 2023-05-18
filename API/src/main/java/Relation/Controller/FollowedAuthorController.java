package Relation.Controller;

import Relation.EmbededClass.FollowedAuthorsId;
import Relation.RelationClass.FollowedAuthor;
import Relation.ServiceClass.FollowedAuthorService;
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

    @PostMapping
    public ResponseEntity<FollowedAuthor> followAuthor(@RequestBody FollowedAuthor followedAuthor) {
        FollowedAuthor newFollowedAuthor = followedAuthorService.followAuthor(followedAuthor);
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
    public ResponseEntity<Void> unfollowAuthor(@PathVariable String authorId, @PathVariable String followerAuthorId) {
        FollowedAuthorsId id = new FollowedAuthorsId(authorId, followerAuthorId);
        followedAuthorService.unfollowAuthor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
