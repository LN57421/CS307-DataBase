package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.FavoritePost;
import com.example.databasefinalproject.Entity.LikedPost;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LikedPostMapper {

    @Select("SELECT * FROM liked_posts WHERE post_id = #{postId} AND liking_author_id = #{authorId}")
    List<LikedPost> findLikedPostsByAuthorIdAndPostName(@Param("postId") int postId, @Param("authorId") String authorId);

    @Insert("INSERT INTO liked_posts (post_id, liking_author_id) VALUES (#{postId}, #{authorId})")
    int createRelation(@Param("postId") int postId, @Param("authorId") String authorId);

    @Delete("DELETE FROM liked_posts WHERE post_id = #{postId} AND liking_author_id = #{authorId}")
    int deleteRelation(@Param("postId") int postId, @Param("authorId") String authorId);

    @Select("SELECT * FROM liked_posts WHERE liking_author_id = #{authorId}")
    List<LikedPost> findLikedPostsByAuthorId(@Param("authorId") String authorId);

}
