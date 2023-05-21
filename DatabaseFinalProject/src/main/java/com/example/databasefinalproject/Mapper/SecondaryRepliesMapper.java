package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.Reply;
import com.example.databasefinalproject.Entity.SecondaryReply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SecondaryRepliesMapper {
    @Insert("INSERT INTO secondary_replies (secondary_reply_id, content, stars, author_id, reply_id) VALUES (#{secondaryReplyId}, #{content}, #{stars}, #{authorId}, #{replyId})")
    int createSecondaryReply(@Param("secondaryReplyId") int secondaryReplyId, @Param("content") int content, @Param("stars") String stars, @Param("authorId") String authorId, @Param("replyId") int replyId);

    @Select("SELECT * FROM secondary_replies WHERE author_id = #{authorId} AND secondary_reply_id = #{secondaryReplyId}")
    List<SecondaryReply> findSecondaryReplyByAuthorIdAndSecondaryReplyId(@Param("authorId") String authorId, @Param("secondaryReplyId") int secondaryReplyId);

    @Select("SELECT * FROM secondary_replies WHERE author_id = #{authorId}")
    List<SecondaryReply> findSecondaryRepliesByAuthorId(@Param("authorId") String authorId);

    @Delete("DELETE FROM secondary_replies WHERE author_id = #{authorId} AND secondary_reply_id = #{secondaryReplyId}")
    int deleteSecondaryReply(@Param("authorId") String authorId, @Param("secondaryReplyId") int secondaryReplyId);

    @Select("SELECT * FROM secondary_replies WHERE secondary_reply_id = #{replyId}")
    List<SecondaryReply> findSecondReplyByFirstReplyId(int replyId);
}
