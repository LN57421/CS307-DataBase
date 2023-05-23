package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.Blacklist;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlacklistMapper {

    @Insert("INSERT INTO blacklist (author_id, blocked_author_id) VALUES (#{authorId}, #{blockedAuthorId})")
    int addToBlacklist(@Param("authorId")int authorId,
                       @Param("blockedAuthorId")int blockedAuthorId);

    @Delete("DELETE FROM blacklist WHERE id = #{id}")
    int removeFromBlacklist(Integer id);

    @Select("SELECT * FROM blacklist WHERE author_id = #{authorId}")
    List<Blacklist> getBlacklistByAuthorId(int authorId);
}
