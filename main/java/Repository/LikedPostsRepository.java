package Repository;

import EmbededClass.LikedPostsId;
import TableClass.LikedPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikedPostsRepository extends JpaRepository<LikedPost, LikedPostsId> {
    LikedPost findByUserIdAndPostId(String userId, Integer postId);

    List<LikedPost> findByUserId(String userId);
}
