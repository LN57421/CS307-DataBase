package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.FollowedAuthor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FollowsMapper {
    @Select("select * from followed_authors where author_id = #{author_id} and follower_author_id = #{follower_author_id}")
    List<FollowedAuthor> findFollowedByAuthorIdAndFollowerId(String author_id, String follower_author_id);

    @Insert("insert into followed_authors(author_id, follower_author_id) values(#{author_id}, #{follower_author_id})")
    int createRelation(String author_id, String follower_author_id);

    @Delete("delete from followed_authors where author_id = #{author_id} and follower_author_id = #{follower_author_id}")
    int deleteRelation(String author_id, String follower_author_id);

    @Select("select * from followed_authors where author_id = #{author_id}")
    List<FollowedAuthor> findFollowedByAuthorId(String author_id);
}
