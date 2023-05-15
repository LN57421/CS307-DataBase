package Controller;

import TableClass.Reply;
import ServiceClass.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/replies")
    public ResponseEntity<Reply> addReply(@RequestBody Reply newReply) {
        try {
            Reply reply = replyService.addReply(newReply);
            return new ResponseEntity<>(reply, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

