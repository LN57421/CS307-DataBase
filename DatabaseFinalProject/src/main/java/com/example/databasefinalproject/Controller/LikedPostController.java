package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.LikedPost;
import com.example.databasefinalproject.Mapper.LikedPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{authorId}/likedPosts")
public class LikedPostController {
    @Autowired
    private LikedPostMapper likedPostMapper;

    @PostMapping("create/{postName}")
    public ResponseEntity<Void> createRelation(@PathVariable String authorId, @PathVariable String postName) {
        if (likedPostMapper.findLikedPostsByAuthorIdAndPostName(authorId, postName) != null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (likedPostMapper.createRelation(authorId, postName) > 0){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{postName}")
    public ResponseEntity<Void> deleteLikedPostRelation(@PathVariable String authorId,@PathVariable String postName) {
        if (likedPostMapper.findLikedPostsByAuthorIdAndPostName(authorId, postName) == null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (likedPostMapper.deleteRelation(authorId, postName) > 0){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/showAll")
    public ResponseEntity<List<LikedPost>> findLikedPostsByAuthorId(@PathVariable String authorId) {
        List<LikedPost> likedPosts = likedPostMapper.findLikedPostsByAuthorId(authorId);
        if (likedPosts.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(likedPosts);
        }
    }

    @DeleteMapping("/showOne/{postName}")
    public ResponseEntity<List<LikedPost>> findLikedPostsByAuthorIdAndPostName(@PathVariable String authorId,@PathVariable String postName){
        List<LikedPost> likedPosts = likedPostMapper.findLikedPostsByAuthorIdAndPostName(authorId,postName);
        if (likedPosts.isEmpty()) {
            return ResponseEntity.notFound().build(); // 返回404 Not Found
        } else {
            return ResponseEntity.ok(likedPosts); // 返回帖子列表
        }
    }
}
