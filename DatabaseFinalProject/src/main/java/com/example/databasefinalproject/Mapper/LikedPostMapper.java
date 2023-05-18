package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.LikedPost;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LikedPostMapper {
    @Select("SELECT * FROM liked_posts WHERE author_id = #{authorId} AND post_name = #{postName}")
    List<LikedPost> findLikedPostsByAuthorIdAndPostName(@Param("authorId") String authorId, @Param("postName") String postName);

    @Insert("INSERT INTO liked_posts (author_id, post_name) VALUES (#{authorId}, #{postName})")
    int createRelation(@Param("authorId") String authorId, @Param("postName") String postName);

    @Delete("DELETE FROM liked_posts WHERE author_id = #{authorId} AND post_name = #{postName}")
    int deleteRelation(@Param("authorId") String authorId, @Param("postName") String postName);

    @Select("SELECT * FROM liked_posts WHERE author_id = #{authorId}")
    List<LikedPost> findLikedPostsByAuthorId(@Param("authorId") String authorId);

}
