package Relation.Repository;


import Relation.EmbededClass.PostCategoriesId;
import Relation.RelationClass.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoriesRepository extends JpaRepository<PostCategory, PostCategoriesId> {
}
