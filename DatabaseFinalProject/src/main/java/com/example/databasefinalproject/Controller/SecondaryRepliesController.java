package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.SecondaryReply;
import com.example.databasefinalproject.Mapper.SecondaryRepliesMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{authorId}/{replyId}/secondaryReplies")
public class SecondaryRepliesController {

    @Autowired
    private SecondaryRepliesMapper secondaryRepliesMapper;

    @ApiOperation("创建一条新的次级回复")
    @PostMapping("/create")
    public ResponseEntity<Void> createSecondaryReply(@PathVariable String authorId, String content,@PathVariable int replyId) {
        int secondaryReplyId = findSecondaryRepliesByAuthorId(authorId).size();
        int star = 0;
        if (secondaryRepliesMapper.createSecondaryReply(secondaryReplyId, star, content, authorId, replyId) > 0) {
            return new ResponseEntity<>(HttpStatus.CREATED); // 201 创建成功
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 创建失败
        }
    }

    @ApiOperation("查看所有的次级回复")
    @GetMapping("/show")
    public List<SecondaryReply> findSecondaryRepliesByAuthorId(@PathVariable String authorId) {
        return secondaryRepliesMapper.findSecondaryRepliesByAuthorId(authorId);
    }

    @ApiOperation("删除一条次级回复")
    @DeleteMapping("/delete/{secondaryReplyId}")
    public ResponseEntity<Void> deleteSecondaryReply(@PathVariable String authorId, @PathVariable int secondaryReplyId) {
        if (secondaryRepliesMapper.findSecondaryReplyByAuthorIdAndSecondaryReplyId(authorId, secondaryReplyId) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 未找到
        }
        if (secondaryRepliesMapper.deleteSecondaryReply(authorId, secondaryReplyId) > 0) {
            return new ResponseEntity<>(HttpStatus.OK); // 200 删除成功
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 删除失败
        }
    }
}
