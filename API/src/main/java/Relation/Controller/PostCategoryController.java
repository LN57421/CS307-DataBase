package Relation.Controller;

import Relation.EmbededClass.PostCategoriesId;
import Relation.RelationClass.PostCategory;
import Relation.ServiceClass.PostCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/postCategories")
public class PostCategoryController {
    private final PostCategoryService postCategoryService;

    @Autowired
    public PostCategoryController(PostCategoryService postCategoryService) {
        this.postCategoryService = postCategoryService;
    }

    @PostMapping
    public PostCategory addPostCategory(@RequestBody PostCategory postCategory) {
        return postCategoryService.addPostCategory(postCategory);
    }

    @GetMapping
    public List<PostCategory> findAllPostCategories() {
        return postCategoryService.findAllPostCategories();
    }

    @PutMapping
    public PostCategory updatePostCategory(@RequestBody PostCategory postCategory) {
        return postCategoryService.updatePostCategory(postCategory);
    }

    @DeleteMapping(path = "/{id}")
    public void deletePostCategory(@PathVariable PostCategoriesId id) {
        postCategoryService.deletePostCategory(id);
    }
}

