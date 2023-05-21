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

    @PostMapping("create/{postId}")
    public ResponseEntity<Void> createRelation(@PathVariable("postId") int postId, @PathVariable("authorId") String authorId) {
        if (likedPostMapper.findLikedPostsByAuthorIdAndPostName(postId, authorId).size() != 0){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (likedPostMapper.createRelation(postId, authorId) > 0){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deleteLikedPostRelation(@PathVariable("postId") int postId, @PathVariable("authorId") String authorId) {
        if (likedPostMapper.findLikedPostsByAuthorIdAndPostName(postId, authorId).size() == 0 ){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (likedPostMapper.deleteRelation(postId, authorId) > 0){
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

    @DeleteMapping("/showOne/{postId}")
    public ResponseEntity<List<LikedPost>> findLikedPostsByAuthorIdAndPostName(@PathVariable("postId") int postId, @PathVariable("authorId") String authorId){
        List<LikedPost> likedPosts = likedPostMapper.findLikedPostsByAuthorIdAndPostName(postId, authorId);
        if (likedPosts.isEmpty()) {
            return ResponseEntity.notFound().build(); // 返回404 Not Found
        } else {
            return ResponseEntity.ok(likedPosts); // 返回帖子列表
        }
    }
}
