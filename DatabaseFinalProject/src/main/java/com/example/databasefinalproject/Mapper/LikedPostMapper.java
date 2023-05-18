package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.LikedPost;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LikedPostMapper {
    @Select("select * from liked_posts where author_id = #{authorId} and post_name = #{postName}")
    List<LikedPost> findLikedPostsByAuthorIdAndPostName(String authorId, String postName);

    @Insert("insert into liked_posts(author_id, post_name) values(#{authorId}, #{postName})")
    int createRelation(String authorId, String postName);

    @Delete("delete from liked_posts where author_id = #{authorId} and post_name = #{postName}")
    int deleteRelation(String authorId, String postName);

    @Select("select * from liked_posts where author_id = #{authorId}")
    List<LikedPost> findLikedPostsByAuthorId(String authorId);
}
