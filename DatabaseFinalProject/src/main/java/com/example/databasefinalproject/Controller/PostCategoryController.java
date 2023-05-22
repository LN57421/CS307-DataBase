package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.PostCategory;
import com.example.databasefinalproject.Mapper.PostCategoryMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/postcategories")
public class PostCategoryController {
    @Autowired
    private PostCategoryMapper postCategoryMapper;

    @ApiOperation("创建文章分类")
    @PostMapping("/create/{postId}")
    public ResponseEntity<Void> createPostCategory(@PathVariable Integer postId, Integer categoryId) {
        if (postCategoryMapper.createPostCategory(postId, categoryId) > 0) {
            return new ResponseEntity<>(HttpStatus.CREATED); // 201 创建成功
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 创建失败
        }
    }

    @ApiOperation("通过文章ID查找文章分类")
    @GetMapping("/findbypostId/{postId}")
    public List<PostCategory> findPostCategoriesByPostId(@PathVariable Integer postId) {
        return postCategoryMapper.findPostCategoriesByPostId(postId);
    }

    @ApiOperation("通过分类ID查找文章分类")
    @GetMapping("/findbycategoryId/{categoryId}")
    public List<PostCategory> findPostCategoriesByCategoryId(@PathVariable Integer categoryId) {
        return postCategoryMapper.findPostCategoriesByCategoryId(categoryId);
    }

    @ApiOperation("删除文章分类")
    @DeleteMapping("/delete/{postId}/{categoryId}")
    public ResponseEntity<Void> deletePostCategory(@PathVariable Integer postId, @PathVariable Integer categoryId) {
        if (postCategoryMapper.deletePostCategory(postId, categoryId) > 0) {
            return new ResponseEntity<>(HttpStatus.OK); // 200 删除成功
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 删除失败
        }
    }
}
