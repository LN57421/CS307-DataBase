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

    @PostMapping("create/{postName}")
    public ResponseEntity<Void> createRelation(@PathVariable String authorId, @PathVariable String postName) {
        if (sharedPostMapper.findRelationByAuthorIdAndPostId(authorId, postName) != null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (sharedPostMapper.createRelation(authorId, postName) > 0){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{postName}")
    public ResponseEntity<Void> deleteSharedPostRelation(@PathVariable String authorId,@PathVariable String postName) {
        if (sharedPostMapper.findRelationByAuthorIdAndPostId(authorId, postName) == null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (sharedPostMapper.deleteRelation(authorId, postName) > 0){
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

    @DeleteMapping("/showOne/{postName}")
    public ResponseEntity<List<SharedPost>> findLikedPostsByAuthorIdAndPostName(@PathVariable String authorId, @PathVariable String postName){
        List<SharedPost> sharedPosts = sharedPostMapper.findRelationByAuthorIdAndPostId(authorId,postName);
        if (sharedPosts.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(sharedPosts);
        }
    }
}
