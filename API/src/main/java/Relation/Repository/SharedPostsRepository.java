package Relation.Repository;

import Relation.EmbededClass.SharedPostsId;
import Relation.RelationClass.SharedPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedPostsRepository extends JpaRepository<SharedPost, SharedPostsId> {
}

