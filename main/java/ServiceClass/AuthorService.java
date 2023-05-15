package ServiceClass;

import Repository.AuthorRepository;
import Repository.ReplyRepository;
import Repository.SecondaryReplyRepository;
import TableClass.Author;
import TableClass.Post;
import TableClass.Reply;
import TableClass.SecondaryReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final ReplyRepository replyRepository;

    private final SecondaryReplyRepository secondaryReplyRepository;

    public AuthorService(AuthorRepository authorRepository, BCryptPasswordEncoder passwordEncoder, ReplyRepository replyRepository, SecondaryReplyRepository secondaryReplyRepository) {
        this.authorRepository = authorRepository;
        this.passwordEncoder = passwordEncoder;
        this.replyRepository = replyRepository;
        this.secondaryReplyRepository = secondaryReplyRepository;
    }

    public Author addAuthor(Author author) {
        // 输入验证 创建过程中author名字不能为空等

        // 如果需要 对密码进行加密
       // author.setPassword(passwordEncoder.encode(author.getPassword()));

        // 保存用户信息
        return authorRepository.save(author);
    }

    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    public Author updateAuthor(Author author) {
        return authorRepository.save(author);
    }

    public void deleteAuthor(String id) {
        authorRepository.deleteById(id);
    }


    /// 可能这几个方法直接用ID就可以查了
    public List<Post> findPostsByAuthor(Author author) {
        return authorRepository.findByAuthor(author);
    }

    public List<Reply> findRepliesByAuthor(Author author) {
        return replyRepository.findByAuthor(author);
    }

    public List<SecondaryReply> findSecondaryRepliesByAuthor(Author author) {
        return secondaryReplyRepository.findByAuthor(author);
    }
}
