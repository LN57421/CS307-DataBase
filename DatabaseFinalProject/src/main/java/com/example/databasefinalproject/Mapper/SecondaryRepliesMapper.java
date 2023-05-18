package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.SecondaryReply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SecondaryRepliesMapper {
    @Insert("insert into secondary_replies(secondary_reply_id, content, stars, author_id, reply_id) values(#{secondaryReplyId}, #{content}, #{stars}, #{authorId}, #{replyId})")
    int createSecondaryReply(int secondaryReplyId, int content, String stars, String authorId, int replyId);

    @Select("select * from secondary_replies where author_id = #{authorId} and secondary_reply_id = #{secondaryReplyId}")
    List<SecondaryReply> findSecondaryReplyByAuthorIdAndSecondaryReplyId(String authorId, int secondaryReplyId);

    @Select("select * from secondary_replies where author_id = #{authorId}")
    List<SecondaryReply> findSecondaryRepliesByAuthorId(String authorId);

    @Delete("delete from secondary_replies where author_id = #{authorId} and secondary_reply_id = #{secondaryReplyId}")
    int deleteSecondaryReply(String authorId, int secondaryReplyId);
}
