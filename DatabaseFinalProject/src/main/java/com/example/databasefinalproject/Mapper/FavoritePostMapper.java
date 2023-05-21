package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.FavoritePost;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FavoritePostMapper {
    @Select("SELECT * FROM favorite_posts WHERE post_id = #{postId} AND favorite_author_id = #{favoriteAuthorId}")
    List<FavoritePost> findFavoritePostsByAuthorIdAndPostName(@Param("postId") int postId, @Param("favoriteAuthorId") String followerAuthorId);

    @Insert("INSERT INTO favorite_posts (post_id, favorite_author_id) VALUES (#{postId}, #{authorId})")
    int createRelation(@Param("postId") int postId, @Param("authorId") String authorId);

    @Delete("DELETE FROM favorite_posts WHERE post_id = #{postId} AND favorite_author_id = #{authorId}")
    int deleteRelation(@Param("postId") int postId, @Param("authorId") String authorId);

    @Select("SELECT * FROM favorite_posts WHERE favorite_author_id = #{authorId}")
    List<FavoritePost> findFavoritePostsByAuthorId(@Param("authorId") String authorId);

}
