package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Insert("INSERT INTO categories (category_id, category_name) VALUES (#{category_id}, #{categoryName})")
    int createCategory(@Param("category_id")int category_id,
                       @Param("category_name")String category_name);

    @Select("SELECT * FROM categories")
    List<Category> getAllCategories();

    @Select("SELECT * FROM categories WHERE category_id = #{categoryId}")
    Category getCategoryById(int categoryId);

    @Delete("DELETE FROM categories WHERE category_id = #{categoryId}")
    int deleteCategory(int categoryId);
}
