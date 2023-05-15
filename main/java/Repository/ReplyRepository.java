package Repository;

import TableClass.Author;
import TableClass.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    List<Reply> findByAuthor(Author author);
}
