package Repository;

import TableClass.FavoritePost;
import EmbededClass.FavoritePostsId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritePostsRepository extends JpaRepository<FavoritePost, FavoritePostsId> {
    FavoritePost findByUserIdAndPostId(String userId, Integer postId);

    List<FavoritePost> findByUserId(Long userId);
}
