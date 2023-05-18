package Entity.Controller;

import Entity.EntityClass.Post;
import Entity.ServiceClass.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public Post addPost(@RequestBody Post post) {
        return postService.addPost(post);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.findAllPosts();
    }

    @PutMapping
    public Post updatePost(@RequestBody Post post) {
        return postService.updatePost(post);
    }

    @DeleteMapping(path = "{postId}")
    public void deletePost(@PathVariable("postId") Integer postId) {
        postService.deletePost(postId);
    }
}

