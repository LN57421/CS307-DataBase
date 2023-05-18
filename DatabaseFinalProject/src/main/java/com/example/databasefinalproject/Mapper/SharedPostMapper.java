package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.SharedPost;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SharedPostMapper {
    @Select("SELECT * FROM shared_posts WHERE author_id = #{authorId} AND post_id = #{postId}")
    List<SharedPost> findRelationByAuthorIdAndPostId(@Param("authorId") String authorId, @Param("postId") String postId);

    @Insert("INSERT INTO shared_posts (author_id, post_id) VALUES (#{authorId}, #{postId})")
    int createRelation(@Param("authorId") String authorId, @Param("postId") String postId);

    @Delete("DELETE FROM shared_posts WHERE author_id = #{authorId} AND post_id = #{postId}")
    int deleteRelation(@Param("authorId") String authorId, @Param("postId") String postId);

    @Select("SELECT * FROM shared_posts WHERE author_id = #{authorId}")
    List<SharedPost> findSharedPostsByAuthorId(@Param("authorId") String authorId);
}
