package Entity.Controller;

import Entity.EntityClass.Reply;
import Entity.ServiceClass.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/replies")
public class ReplyController {

    private final ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping
    public Reply addReply(@RequestBody Reply reply) {
        return replyService.addReply(reply);
    }

    @GetMapping
    public List<Reply> getAllReplies() {
        return replyService.findAllReplies();
    }

    @PutMapping
    public Reply updateReply(@RequestBody Reply reply) {
        return replyService.updateReply(reply);
    }

    @DeleteMapping(path = "{replyId}")
    public void deleteReply(@PathVariable("replyId") Integer replyId) {
        replyService.deleteReply(replyId);
    }
}

