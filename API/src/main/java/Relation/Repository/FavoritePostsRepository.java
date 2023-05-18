package Relation.Repository;

import Relation.EmbededClass.FavoritePostsId;
import Relation.RelationClass.FavoritePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritePostsRepository extends JpaRepository<FavoritePost, FavoritePostsId> {
}
