package Relation.ServiceClass;

import Relation.EmbededClass.SharedPostsId;
import Relation.RelationClass.SharedPost;
import Relation.Repository.SharedPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SharedPostService {
    private final SharedPostsRepository sharedPostRepository;

    @Autowired
    public SharedPostService(SharedPostsRepository sharedPostRepository) {
        this.sharedPostRepository = sharedPostRepository;
    }

    public SharedPost addSharedPost(SharedPost sharedPost) {
        return sharedPostRepository.save(sharedPost);
    }

    public List<SharedPost> findAllSharedPosts() {
        return sharedPostRepository.findAll();
    }

    public SharedPost updateSharedPost(SharedPost sharedPost) {
        return sharedPostRepository.save(sharedPost);
    }

    public void deleteSharedPost(SharedPostsId id) {
        sharedPostRepository.deleteById(id);
    }
}

