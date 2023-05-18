package Relation.Repository;


import Relation.EmbededClass.FollowedAuthorsId;
import Relation.RelationClass.FollowedAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowedAuthorsRepository extends JpaRepository<FollowedAuthor, FollowedAuthorsId> {
}
