package Relation.ServiceClass;

import Relation.RelationClass.LikedPost;
import Relation.EmbededClass.LikedPostsId;
import Relation.Repository.LikedPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikedPostService {
    private final LikedPostsRepository likedPostRepository;

    @Autowired
    public LikedPostService(LikedPostsRepository likedPostRepository) {
        this.likedPostRepository = likedPostRepository;
    }

    public LikedPost addLikedPost(LikedPost likedPost) {
        return likedPostRepository.save(likedPost);
    }

    public List<LikedPost> findAllLikedPosts() {
        return likedPostRepository.findAll();
    }

    public LikedPost updateLikedPost(LikedPost likedPost) {
        return likedPostRepository.save(likedPost);
    }

    public void deleteLikedPost(LikedPostsId id) {
        likedPostRepository.deleteById(id);
    }
}

