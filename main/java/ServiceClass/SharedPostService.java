package ServiceClass;

import EmbededClass.SharedPostsId;
import TableClass.FavoritePost;
import TableClass.LikedPost;
import TableClass.SharedPost;
import Repository.SharedPostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SharedPostService {
    private final SharedPostsRepository sharedPostRepository;

    @Autowired
    public SharedPostService(SharedPostsRepository sharedPostRepository) {
        this.sharedPostRepository = sharedPostRepository;
    }

    public SharedPost  addSharedPost(SharedPost sharedPost) {
        // 检查用户是否已经收藏过这个帖子
        SharedPost existingSharedPost = sharedPostRepository.findByUserIdAndPostId(sharedPost.getId().getSharingAuthorId(), sharedPost.getId().getPostId());
        if (existingSharedPost != null) {
            throw new DBException("用户已经分享过这个帖子");
        }
        return sharedPostRepository.save(sharedPost);
    }

    public List<SharedPost> getSharedPostsByUserId(Long userId) {
        return Optional.ofNullable(sharedPostRepository.findByUserId(userId))
                .orElse(Collections.emptyList());
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

