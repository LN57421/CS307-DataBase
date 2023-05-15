package Controller;
import TableClass.SecondaryReply;
import ServiceClass.SecondaryReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/secondaryreplies")
    public ResponseEntity<SecondaryReply> addSecondaryReply(@RequestBody SecondaryReply newSecondaryReply) {
        try {
            SecondaryReply secondaryReply = secondaryReplyService.addSecondaryReply(newSecondaryReply);
            return new ResponseEntity<>(secondaryReply, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

