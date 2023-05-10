package Relation.Controller;

import Relation.EmbededClass.LikedPostsId;
import Relation.RelationClass.LikedPost;
import Relation.ServiceClass.LikedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/likedPosts")
public class LikedPostController {
    private final LikedPostService likedPostService;

    @Autowired
    public LikedPostController(LikedPostService likedPostService) {
        this.likedPostService = likedPostService;
    }

    @PostMapping
    public LikedPost addLikedPost(@RequestBody LikedPost likedPost) {
        return likedPostService.addLikedPost(likedPost);
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
