package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.Reply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RepliesMapper {
    @Insert("insert into replies(reply_id, content, stars, author_id, post_id) values(#{replyId}, #{content}, #{stars}, #{authorId}, #{postId})")
    int createReply(int replyId, int content, String stars, String authorId, String postId);

    @Select("select * from replies where author_id = #{authorId} and reply_id = #{replyId}")
    List<Reply> findReplyByAuthorIdAndReplyId(String authorId, int replyId);


    @Select("select * from replies where author_id = #{authorId}")
    List<Reply> findRepliesByAuthorId(String authorId);

    @Delete("delete from replies where author_id = #{authorId} and reply_id = #{replyId}")
    int deleteReply(String authorId, int replyId);


}
