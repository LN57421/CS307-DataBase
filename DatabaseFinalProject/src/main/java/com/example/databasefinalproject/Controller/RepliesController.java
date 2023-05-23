package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.Reply;
import com.example.databasefinalproject.Mapper.RepliesMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{authorId}/{postID}/replies")
public class RepliesController {

    @Autowired
    private RepliesMapper repliesMapper;

    @ApiOperation("创建一条新回复")
    @PostMapping("/create")
    public ResponseEntity<Void> createReply(@PathVariable String authorId, String content,@PathVariable String postID, boolean is_anonymous) {
        int replyId = findRepliesByAuthorId(authorId).size();
        int star = 0;
        if (repliesMapper.createReply(replyId, star, content, authorId, postID, is_anonymous) > 0) {
            return new ResponseEntity<>(HttpStatus.CREATED); // 201 创建成功
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 创建失败
        }
    }

    @ApiOperation("查看所有的回复")
    @GetMapping("/show")
    public List<Reply> findRepliesByAuthorId(@PathVariable String authorId) {
        return repliesMapper.findRepliesByAuthorId(authorId);
    }

    @ApiOperation("针对某一条回复进行删除")
    @DeleteMapping("/delete/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable String authorId, @PathVariable int replyId) {
        if (repliesMapper.findReplyByAuthorIdAndReplyId(authorId, replyId) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 未找到
        }
        if (repliesMapper.deleteReply(authorId, replyId) > 0) {
            return new ResponseEntity<>(HttpStatus.OK); // 200 删除成功
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 删除失败
        }
    }
}