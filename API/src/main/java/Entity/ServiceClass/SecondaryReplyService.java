package Entity.ServiceClass;

import Entity.EntityClass.*;
import Entity.Repository.SecondaryReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SecondaryReplyService {
    private final SecondaryReplyRepository secondaryReplyRepository;

    @Autowired
    public SecondaryReplyService(SecondaryReplyRepository secondaryReplyRepository) {
        this.secondaryReplyRepository = secondaryReplyRepository;
    }

    public SecondaryReply addSecondaryReply(SecondaryReply secondaryReply) {
        return secondaryReplyRepository.save(secondaryReply);
    }

    public List<SecondaryReply> findAllSecondaryReplies() {
        return secondaryReplyRepository.findAll();
    }

    public SecondaryReply updateSecondaryReply(SecondaryReply secondaryReply) {
        return secondaryReplyRepository.save(secondaryReply);
    }

    public void deleteSecondaryReply(Integer id) {
        secondaryReplyRepository.deleteById(id);
    }
}

