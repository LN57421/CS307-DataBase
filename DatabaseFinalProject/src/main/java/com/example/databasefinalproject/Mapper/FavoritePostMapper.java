package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.FavoritePost;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FavoritePostMapper {
    @Select("SELECT * FROM favorite_posts WHERE author_id = #{authorId} AND title = #{postName}")
    List<FavoritePost> findFavoritePostsByAuthorIdAndPostName(@Param("authorId") String authorId, @Param("postName") String postName);

    @Insert("INSERT INTO favorite_posts (author_id, title) VALUES (#{authorId}, #{postName})")
    int createRelation(@Param("authorId") String authorId, @Param("postName") String postName);

    @Delete("DELETE FROM favorite_posts WHERE author_id = #{authorId} AND title = #{postName}")
    int deleteRelation(@Param("authorId") String authorId, @Param("postName") String postName);

    @Select("SELECT * FROM favorite_posts WHERE author_id = #{authorId}")
    List<FavoritePost> findFavoritePostsByAuthorId(@Param("authorId") String authorId);

}
