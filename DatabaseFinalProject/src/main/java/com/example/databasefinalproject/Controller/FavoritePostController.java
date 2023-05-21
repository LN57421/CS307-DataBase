package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.FavoritePost;
import com.example.databasefinalproject.Mapper.FavoritePostMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{authorId}/favoritePosts")
public class  FavoritePostController {

    @Autowired
    private FavoritePostMapper favoritePostMapper;

    @ApiOperation("author收藏了某个帖子")
    @PostMapping("create/{postId}")
    public ResponseEntity<Void> createRelation(@PathVariable("postId") int postId, @PathVariable("authorId") String authorId) {
        if (favoritePostMapper.findFavoritePostsByAuthorIdAndPostName(postId, authorId).size() != 0){//不能用null
            return new ResponseEntity<>(HttpStatus.CONFLICT);// 409 已经收藏
        }
        if (favoritePostMapper.createRelation(postId, authorId) > 0){
            return new ResponseEntity<>(HttpStatus.CREATED); // 201 创建成功
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 创建失败
        }
    }

    @ApiOperation("author不收藏某个帖子")
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deleteFavoritePostRelation(@PathVariable("postId") int postId, @PathVariable("authorId") String authorId) {
        if (favoritePostMapper.findFavoritePostsByAuthorIdAndPostName(postId, authorId).size() == 0){
            return new ResponseEntity<>(HttpStatus.CONFLICT);// 409 已经删除
        }
        if (favoritePostMapper.deleteRelation(postId, authorId) > 0){
            return new ResponseEntity<>(HttpStatus.CREATED); // 201 删除成功
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 删除失败
        }
    }

    @ApiOperation("author收藏的所有帖子")
    @GetMapping("/showAll")
    public ResponseEntity<List<FavoritePost>> findFavoritePostsByAuthorId(@PathVariable String authorId) {
        List<FavoritePost> favoritePosts = favoritePostMapper.findFavoritePostsByAuthorId(authorId);
        if (favoritePosts.isEmpty()) {
            return ResponseEntity.notFound().build(); // 返回404 Not Found
        } else {
            return ResponseEntity.ok(favoritePosts); // 返回帖子列表
        }
    }

    @ApiOperation("查找author有没有收藏某个帖子")
    @DeleteMapping("/show/{postId}")
    public ResponseEntity<List<FavoritePost>> findFavoritePostsByAuthorIdAndPostName(@PathVariable("postId") int postId, @PathVariable("authorId") String authorId){
        List<FavoritePost> favoritePosts = favoritePostMapper.findFavoritePostsByAuthorIdAndPostName(postId,authorId);
        if (favoritePosts.isEmpty()) {
            return ResponseEntity.notFound().build(); // 返回404 Not Found
        } else {
            return ResponseEntity.ok(favoritePosts); // 返回帖子列表
        }
    }
}
