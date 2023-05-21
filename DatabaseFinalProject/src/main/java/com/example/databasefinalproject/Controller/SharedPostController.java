package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.SharedPost;
import com.example.databasefinalproject.Mapper.SharedPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{authorId}/sharedPosts")
public class SharedPostController {

    @Autowired
    private SharedPostMapper sharedPostMapper;

    @PostMapping("create/{postId}")
    public ResponseEntity<Void> createRelation(@PathVariable("postId") int postId, @PathVariable("authorId") String authorId) {
        if (sharedPostMapper.findRelationByAuthorIdAndPostId(authorId, postId).size() != 0){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (sharedPostMapper.createRelation(authorId, postId) > 0){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deleteSharedPostRelation(@PathVariable("postId") int postId, @PathVariable("authorId") String authorId) {
        if (sharedPostMapper.findRelationByAuthorIdAndPostId(authorId, postId).size() == 0){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (sharedPostMapper.deleteRelation(authorId, postId) > 0){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/showAll")
    public ResponseEntity<List<SharedPost>> findSharedPostsByAuthorId(@PathVariable String authorId) {
        List<SharedPost> sharedPosts = sharedPostMapper.findSharedPostsByAuthorId(authorId);
        if (sharedPosts.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(sharedPosts);
        }
    }

    @DeleteMapping("/showOne/{postId}")
    public ResponseEntity<List<SharedPost>> findLikedPostsByAuthorIdAndPostName(@PathVariable("postId") int postId, @PathVariable("authorId") String authorId){
        List<SharedPost> sharedPosts = sharedPostMapper.findRelationByAuthorIdAndPostId(authorId,postId);
        if (sharedPosts.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(sharedPosts);
        }
    }
}
