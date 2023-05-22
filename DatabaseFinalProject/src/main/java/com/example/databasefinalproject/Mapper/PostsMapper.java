package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface PostsMapper {
    @Insert("insert into posts(post_id, author_id, title, content, posting_time, posting_city) values(#{postId}, #{authorId}, #{title}, #{content}, #{time}, #{city})")
    int createPost(@Param("postId") int postId,
                   @Param("authorId") String authorId,
                   @Param("title") String title,
                   @Param("content") String content,
                   @Param("time") Timestamp time,
                   @Param("city") String city );

    @Select("select * from posts where author_id = #{authorId}")
    List<Post> findPostsByAuthorId(String authorId);

    @Select("select * from posts")
    List<Post> getAllPost();

    @Select("select * from posts where post_id = #{postId}")
    Post findPostByPostId(int postId);
}
