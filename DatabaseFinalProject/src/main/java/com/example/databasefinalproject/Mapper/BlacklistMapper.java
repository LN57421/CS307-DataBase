package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.Blacklist;
import com.example.databasefinalproject.Entity.FollowedAuthor;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlacklistMapper {

    @Insert("INSERT INTO blacklist (author_id, blocked_author_id) VALUES (#{authorId}, #{blockedAuthorId})")
    int addToBlacklist(@Param("authorId")String authorId,
                       @Param("blockedAuthorId")String blockedAuthorId);

    @Delete("DELETE FROM blacklist WHERE blocked_author_id = #{blocked_author_id} AND author_id = #{authorId}")
    int removeFromBlacklist(@Param("blocked_author_id") String id, @Param("authorId") String authorId);

    @Select("SELECT * FROM blacklist WHERE author_id = #{authorId}")
    List<Blacklist> getBlacklistByAuthorId(String authorId);

    @Select("SELECT * FROM blacklist WHERE author_id = #{authorId} AND blocked_author_id = #{blocked_author_id}")
    List<Blacklist> findBlackByAuthorIdAndOthors(@Param("authorId") String authorId, @Param("blocked_author_id") String blockedAuthorId);
}
