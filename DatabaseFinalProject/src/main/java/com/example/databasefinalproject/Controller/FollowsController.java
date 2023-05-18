package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.FollowedAuthor;
import com.example.databasefinalproject.Mapper.FollowsMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{author_id}/follows")
public class FollowsController {

    @Autowired
    private FollowsMapper followsMapper;


    @ApiOperation("当前用户关注某个用户")
    @PostMapping("create/{follower_author_id}")
    public ResponseEntity<Void> createRelation(@PathVariable String author_id, @PathVariable String follower_author_id) {
        if (followsMapper.findFollowedByAuthorIdAndFollowerId(author_id, follower_author_id) != null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (followsMapper.createRelation(author_id, follower_author_id) > 0){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("当前用户不再关注某个用户")
    @DeleteMapping("/delete/{follower_author_id}")
    public ResponseEntity<Void> deleteFollowRelation(@PathVariable String author_id, @PathVariable String follower_author_id) {
        if (followsMapper.findFollowedByAuthorIdAndFollowerId(author_id, follower_author_id) == null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (followsMapper.deleteRelation(author_id, follower_author_id) > 0){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("查看当前用户的所有关注")
    @GetMapping("/showAll")
    public ResponseEntity<List<FollowedAuthor>> findFollowedByAuthorId(@PathVariable String author_id) {
        List<FollowedAuthor> followedUsers = followsMapper.findFollowedByAuthorId(author_id);
        if (followedUsers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(followedUsers);
        }
    }

    @ApiOperation("查看当前用户是否有某个特定关注")
    @GetMapping("/showOne/{follower_author_id}")
    public ResponseEntity<List<FollowedAuthor>> findFollowedByAuthorIdAndFollowerId(@PathVariable String author_id, @PathVariable String follower_author_id) {
        List<FollowedAuthor> followRelations = followsMapper.findFollowedByAuthorIdAndFollowerId(author_id, follower_author_id);
        if (followRelations.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(followRelations);
        }
    }
}

