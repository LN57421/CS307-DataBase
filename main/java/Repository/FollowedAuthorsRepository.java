package Repository;


import EmbededClass.FollowedAuthorsId;
import TableClass.FollowedAuthor;
import TableClass.LikedPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowedAuthorsRepository extends JpaRepository<FollowedAuthor, FollowedAuthorsId> {
    List<FollowedAuthor> findAllByAuthorID(String userId);
}
