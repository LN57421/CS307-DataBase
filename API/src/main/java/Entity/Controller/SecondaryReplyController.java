package Entity.Controller;
import Entity.EntityClass.SecondaryReply;
import Entity.ServiceClass.SecondaryReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/secondary_replies")
public class SecondaryReplyController {

    private final SecondaryReplyService secondaryReplyService;

    @Autowired
    public SecondaryReplyController(SecondaryReplyService secondaryReplyService) {
        this.secondaryReplyService = secondaryReplyService;
    }

    @PostMapping
    public SecondaryReply addSecondaryReply(@RequestBody SecondaryReply secondaryReply) {
        return secondaryReplyService.addSecondaryReply(secondaryReply);
    }

    @GetMapping
    public List<SecondaryReply> getAllSecondaryReplies() {
        return secondaryReplyService.findAllSecondaryReplies();
    }

    @PutMapping
    public SecondaryReply updateSecondaryReply(@RequestBody SecondaryReply secondaryReply) {
        return secondaryReplyService.updateSecondaryReply(secondaryReply);
    }

    @DeleteMapping(path = "{secondaryReplyId}")
    public void deleteSecondaryReply(@PathVariable("secondaryReplyId") Integer secondaryReplyId) {
        secondaryReplyService.deleteSecondaryReply(secondaryReplyId);
    }
}

