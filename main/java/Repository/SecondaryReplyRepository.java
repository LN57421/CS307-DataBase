package Repository;

import TableClass.Author;
import TableClass.SecondaryReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecondaryReplyRepository extends JpaRepository<SecondaryReply, Integer> {
    List<SecondaryReply> findByAuthor(Author author);
}
