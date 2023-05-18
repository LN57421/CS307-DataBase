package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.Post;
import com.example.databasefinalproject.Mapper.PostsMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{authorId}/posts")
public class PostsController {
    @Autowired
    private PostsMapper postsMapper;

    @ApiOperation("创建新的帖子")
    @PostMapping("/create")
    public ResponseEntity<Void> createPost(@PathVariable String authorId, String title, String content) {
        int postId = postsMapper.findPostsByAuthorId(authorId).size();
        if (postsMapper.createPost(postId, authorId, title, content) > 0){
            return new ResponseEntity<>(HttpStatus.CREATED);//201
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);//500
        }
    }


    @ApiOperation("查找用户创建的所有帖子")
    @GetMapping("/show")
    public List<Post> findPostsByAuthorId(@PathVariable String authorId) {
        return postsMapper.findPostsByAuthorId(authorId);
    }
}
