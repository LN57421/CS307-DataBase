package Repository;


import EmbededClass.PostCategoriesId;
import TableClass.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoriesRepository extends JpaRepository<PostCategory, PostCategoriesId> {
}
