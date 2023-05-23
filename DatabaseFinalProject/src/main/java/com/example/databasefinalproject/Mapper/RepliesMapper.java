package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.Reply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RepliesMapper {
    @Insert("INSERT INTO replies (reply_id, content, stars, author_id, post_id, is_anonymous) VALUES (#{replyId}, #{content}, #{stars}, #{authorId}, #{postId}, #{is_anonymous)")
    int createReply(@Param("replyId") int replyId,
                    @Param("content") int content,
                    @Param("stars") String stars,
                    @Param("authorId") String authorId,
                    @Param("postId") String postId,
                    @Param("is_anonymous") boolean is_anonymous);

    @Select("SELECT * FROM replies WHERE author_id = #{authorId} AND reply_id = #{replyId}")
    List<Reply> findReplyByAuthorIdAndReplyId(@Param("authorId") String authorId, @Param("replyId") int replyId);

    @Select("SELECT * FROM replies WHERE author_id = #{authorId} AND is_anonymous = false")
    List<Reply> findRepliesByAuthorId(@Param("authorId") String authorId);

    @Delete("DELETE FROM replies WHERE author_id = #{authorId} AND reply_id = #{replyId}")
    int deleteReply(@Param("authorId") String authorId, @Param("replyId") int replyId);

    @Select("SELECT * FROM replies WHERE post_id = #{postId}")
    List<Reply> findReplyByPostId(int postId);

}
