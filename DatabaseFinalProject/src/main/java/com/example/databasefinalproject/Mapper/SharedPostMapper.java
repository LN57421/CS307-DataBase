package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.SharedPost;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SharedPostMapper {
    @Select("SELECT * FROM shared_posts WHERE sharing_author_id = #{authorId} AND post_id = #{postId}")
    List<SharedPost> findRelationByAuthorIdAndPostId(@Param("authorId") String authorId, @Param("postId") int postId);

    @Insert("INSERT INTO shared_posts (post_id,sharing_author_id) VALUES (#{postId}, #{authorId})")
    int createRelation(@Param("authorId") String authorId, @Param("postId") int postId);

    @Delete("DELETE FROM shared_posts WHERE sharing_author_id = #{authorId} AND post_id = #{postId}")
    int deleteRelation(@Param("authorId") String authorId, @Param("postId") int postId);

    @Select("SELECT * FROM shared_posts WHERE sharing_author_id = #{authorId}")
    List<SharedPost> findSharedPostsByAuthorId(@Param("authorId") String authorId);
}
