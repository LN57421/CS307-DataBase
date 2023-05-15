package ServiceClass;


import Repository.ReplyRepository;
import TableClass.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReplyService {
    private final ReplyRepository replyRepository;

    @Autowired
    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public Reply addReply(Reply reply) {
        return replyRepository.save(reply);
    }

    public List<Reply> findAllReplies() {
        return replyRepository.findAll();
    }

    public Reply updateReply(Reply reply) {
        return replyRepository.save(reply);
    }

    public void deleteReply(Integer id) {
        replyRepository.deleteById(id);
    }
}

