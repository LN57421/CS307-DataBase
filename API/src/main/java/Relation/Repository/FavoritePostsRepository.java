package Relation.Repository;

import Relation.RelationClass.FavoritePost;
import Relation.EmbededClass.FavoritePostsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritePostsRepository extends JpaRepository<FavoritePost, FavoritePostsId> {
}
