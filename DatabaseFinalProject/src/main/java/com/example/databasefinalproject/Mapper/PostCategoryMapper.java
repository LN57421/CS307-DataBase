package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.PostCategory;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface PostCategoryMapper {
    @Insert("insert into post_categories(post_id, category_id) values(#{postId}, #{categoryId})")
    int createPostCategory(@Param("postId")Integer postId, @Param("categoryId")Integer categoryId);

    @Select("select * from post_categories where post_id = #{postId}")
    List<PostCategory> findPostCategoriesByPostId(Integer postId);

    @Select("select * from post_categories where category_id = #{categoryId}")
    List<PostCategory> findPostCategoriesByCategoryId(Integer categoryId);

    @Delete("delete from post_categories where post_id = #{postId} and category_id = #{categoryId}")
    int deletePostCategory(@Param("postId")Integer postId, @Param("categoryId")Integer categoryId);
}
