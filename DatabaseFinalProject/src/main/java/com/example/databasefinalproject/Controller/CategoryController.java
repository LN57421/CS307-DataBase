package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.Category;
import com.example.databasefinalproject.Mapper.CategoryMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryMapper categoryMapper;

    @ApiOperation("创建新的分类")
    @PostMapping("/create")
    public ResponseEntity<Void> createCategory(String category_name) {
        int id = categoryMapper.getAllCategories().size();
        if (categoryMapper.createCategory(id,category_name) > 0) {
            return new ResponseEntity<>(HttpStatus.CREATED); // 201 创建成功
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 创建失败
        }
    }

    @ApiOperation("获取所有分类")
    @GetMapping("/show")
    public List<Category> getAllCategories() {
        return categoryMapper.getAllCategories();
    }

    @ApiOperation("根据ID获取分类")
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById() {
        int categoryId = categoryMapper.getAllCategories().size();
        Category category = categoryMapper.getCategoryById(categoryId);
        if (category != null) {
            return new ResponseEntity<>(category, HttpStatus.OK); // 200 查询成功
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 未找到
        }
    }


    @ApiOperation("删除分类")
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer categoryId) {
        if (categoryMapper.deleteCategory(categoryId) > 0) {
            return new ResponseEntity<>(HttpStatus.OK); // 200 删除成功
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 删除失败
        }
    }
}
