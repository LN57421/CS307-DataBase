package Repository;

import TableClass.Author;
import TableClass.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, String> {
    List<Post> findByAuthor(Author author);
}