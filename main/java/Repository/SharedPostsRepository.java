package Repository;

import EmbededClass.SharedPostsId;
import TableClass.FavoritePost;
import TableClass.SharedPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharedPostsRepository extends JpaRepository<SharedPost, SharedPostsId> {
    SharedPost findByUserIdAndPostId(String userId, Integer postId);

    List<SharedPost> findByUserId(Long userId);
}

