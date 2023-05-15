package ServiceClass;

import EmbededClass.LikedPostsId;
import TableClass.LikedPost;
import Repository.LikedPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class LikedPostService {
    private final LikedPostsRepository likedPostRepository;

    @Autowired
    public LikedPostService(LikedPostsRepository likedPostRepository) {
        this.likedPostRepository = likedPostRepository;
    }

    public LikedPost addLikedPost(LikedPost likedPost) {
        // 检查用户是否已点赞过这个帖子
        LikedPost existingLikedPost = likedPostRepository.findByUserIdAndPostId(likedPost.getId().getLikingAuthorId(), likedPost.getId().getPostId());
        if (existingLikedPost != null) {
            throw new DBException("用户已经收藏过这个帖子");
        }
        return likedPostRepository.save(likedPost);
    }

    public List<LikedPost> getLikedPostsByUserId(String userId) {
        return Optional.ofNullable(likedPostRepository.findByUserId(userId))
                .orElse(Collections.emptyList());
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

