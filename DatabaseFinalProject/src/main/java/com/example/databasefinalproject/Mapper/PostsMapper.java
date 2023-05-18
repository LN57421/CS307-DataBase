package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PostsMapper {
    @Insert("insert into posts(post_id, author_id, title, content) values(#{authorId}, #{title}, #{content})")
    int createPost(int postId, String authorId, String title, String content);

    @Select("select * from posts where author_id = #{authorId}")
    List<Post> findPostsByAuthorId(String authorId);
}
