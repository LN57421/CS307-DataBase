package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.FavoritePost;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FavoritePostMapper {

    @Select("select * from favorite_posts where author_id = #{authorId} and post_id = #{postId}")
    FavoritePost findRelationByAuthorIdAndPostId(String authorId, int postId);

    @Insert("insert into favorite_posts(author_id, post_id) values(#{authorId}, #{postId})")
    void createRelation(String authorId, int postId);

    @Delete("delete from favorite_posts where author_id = #{authorId} and post_id = #{postId}")
    void deleteRelation(String authorId, int postId);

    @Select("SELECT * FROM favorite_posts WHERE author_id = #{authorId}")
    List<FavoritePost> findFavoritePostsByAuthorId(String authorId);
}
