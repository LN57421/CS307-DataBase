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
    @PostMapping("create/{postName}")
    public ResponseEntity<Void> createRelation(@PathVariable String authorId, @PathVariable String postName) {
        if (favoritePostMapper.findFavoritePostsByAuthorIdAndPostName(authorId, postName) != null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);// 409 已经收藏
        }
        if (favoritePostMapper.createRelation(authorId, postName) > 0){
            return new ResponseEntity<>(HttpStatus.CREATED); // 201 创建成功
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 创建失败
        }
    }

    @ApiOperation("author不收藏某个帖子")
    @DeleteMapping("/delete/{postName}")
    public ResponseEntity<Void> deleteFavoritePostRelation(@PathVariable String authorId,@PathVariable String postName) {
        if (favoritePostMapper.findFavoritePostsByAuthorIdAndPostName(authorId, postName) == null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);// 409 已经删除
        }
        if (favoritePostMapper.deleteRelation(authorId, postName) > 0){
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
    @DeleteMapping("/show/{postName}")
    public ResponseEntity<List<FavoritePost>> findFavoritePostsByAuthorIdAndPostName(@PathVariable String authorId,@PathVariable String postName){
        List<FavoritePost> favoritePosts = favoritePostMapper.findFavoritePostsByAuthorIdAndPostName(authorId,postName);
        if (favoritePosts.isEmpty()) {
            return ResponseEntity.notFound().build(); // 返回404 Not Found
        } else {
            return ResponseEntity.ok(favoritePosts); // 返回帖子列表
        }
    }
}
