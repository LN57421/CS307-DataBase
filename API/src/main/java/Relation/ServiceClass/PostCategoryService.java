package Relation.ServiceClass;

import Relation.EmbededClass.PostCategoriesId;
import Relation.RelationClass.PostCategory;
import Relation.Repository.PostCategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostCategoryService {

    private final PostCategoriesRepository postCategoryRepository;

    @Autowired
    public PostCategoryService(PostCategoriesRepository postCategoryRepository) {
        this.postCategoryRepository = postCategoryRepository;
    }

    public PostCategory addPostCategory(PostCategory postCategory) {
        return postCategoryRepository.save(postCategory);
    }

    public List<PostCategory> findAllPostCategories() {
        return postCategoryRepository.findAll();
    }

    public PostCategory updatePostCategory(PostCategory postCategory) {
        return postCategoryRepository.save(postCategory);
    }

    public void deletePostCategory(PostCategoriesId id) {
        postCategoryRepository.deleteById(id);
    }
}
