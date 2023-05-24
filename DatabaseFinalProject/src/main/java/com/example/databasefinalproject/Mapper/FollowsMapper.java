package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.FollowedAuthor;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FollowsMapper {
    @Select("SELECT * FROM followed_authors WHERE author_id = #{authorId} AND follower_author_id = #{followerAuthorId}")
    List<FollowedAuthor> findFollowedByAuthorIdAndFollowerId(@Param("authorId") String authorId, @Param("followerAuthorId") String followerAuthorId);

    @Insert("INSERT INTO followed_authors (author_id, follower_author_id) VALUES (#{authorId}, #{followerAuthorId})")
    int createRelation(@Param("authorId") String authorId, @Param("followerAuthorId") String followerAuthorId);

    @Delete("DELETE FROM followed_authors WHERE author_id = #{authorId} AND follower_author_id = #{followerAuthorId}")
    int deleteRelation(@Param("authorId") String authorId, @Param("followerAuthorId") String followerAuthorId);

    @Select("SELECT * FROM followed_authors WHERE author_id = #{authorId}")
    List<FollowedAuthor> findFollowedByAuthorId(@Param("authorId") String authorId);
    @Select("SELECT * FROM followed_authors WHERE follower_author_id = #{authorId}")
    List<FollowedAuthor> findFollowingByAuthorId(@Param("authorId") String authorId);
}
