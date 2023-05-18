package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.FavoritePost;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FavoritePostMapper {
    @Select("select * from favorite_posts where author_id = #{authorId} and title = #{postName}")
    List<FavoritePost> findFavoritePostsByAuthorIdAndPostName(String authorId, String postName);

    @Insert("insert into favorite_posts(author_id, title) values(#{authorId}, #{postName})")
    int createRelation(String authorId, String postName);

    @Delete("delete from favorite_posts where author_id = #{authorId} and title = #{postName}")
    int deleteRelation(String authorId, String postName);

    @Select("select * from favorite_posts where author_id = #{authorId}")
    List<FavoritePost> findFavoritePostsByAuthorId(String authorId);
}
