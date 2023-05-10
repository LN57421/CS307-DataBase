package Relation.Repository;

import Relation.EmbededClass.LikedPostsId;
import Relation.RelationClass.LikedPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedPostsRepository extends JpaRepository<LikedPost, LikedPostsId> {
}
