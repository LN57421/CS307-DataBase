package Relation.Controller;

import Relation.EmbededClass.SharedPostsId;
import Relation.RelationClass.SharedPost;
import Relation.ServiceClass.SharedPostService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public SharedPost addSharedPost(@RequestBody SharedPost sharedPost) {
        return sharedPostService.addSharedPost(sharedPost);
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

