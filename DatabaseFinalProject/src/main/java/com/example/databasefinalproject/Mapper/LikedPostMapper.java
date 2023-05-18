package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.LikedPost;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LikedPostMapper {

    @Select("select * from liked_posts where author_id = #{authorId} and post_id = #{postId}")
    LikedPost findRelationByAuthorIdAndPostId(String authorId, String postId);

    @Insert("insert into liked_posts(author_id, post_id) values(#{authorId}, #{postId})")
    void createRelation(String authorId, String postId);

    @Delete("delete from liked_posts where author_id = #{authorId} and post_id = #{postId}")
    void deleteRelation(String authorId, String postId);

    @Select("SELECT * FROM liked_posts WHERE author_id = #{authorId}")
    List<LikedPost> findLikedPostsByAuthorId(String authorId);

}
