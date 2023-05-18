package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.SharedPost;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SharedPostMapper {
    @Select("select * from shared_posts where author_id = #{authorId} and post_id = #{postId}")
    List<SharedPost> findRelationByAuthorIdAndPostId(String authorId, String postId);

    @Insert("insert into shared_posts(author_id, post_id) values(#{authorId}, #{postId})")
    int createRelation(String authorId, String postId);

    @Delete("delete from shared_posts where author_id = #{authorId} and post_id = #{postId}")
    int deleteRelation(String authorId, String postId);

    @Select("select * from shared_posts where author_id = #{authorId}")
    List<SharedPost> findSharedPostsByAuthorId(String authorId);

}
