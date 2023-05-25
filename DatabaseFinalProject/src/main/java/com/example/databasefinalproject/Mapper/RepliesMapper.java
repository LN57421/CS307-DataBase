package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.Reply;
import com.example.databasefinalproject.Entity.SecondaryReply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RepliesMapper {
    @Insert("INSERT INTO replies (reply_id, content, stars, author_id, post_id, is_anonymous) VALUES (#{replyId}, #{content}, #{stars}, #{authorId}, #{postId}, #{is_anonymous})")
    int createReply(@Param("replyId") int replyId,
                    @Param("content") String content,
                    @Param("stars") int stars,
                    @Param("authorId") String authorId,
                    @Param("postId") int postId,
                    @Param("is_anonymous") boolean is_anonymous);

    @Select("SELECT * FROM replies WHERE author_id = #{authorId} AND reply_id = #{replyId}")
    List<Reply> findReplyByAuthorIdAndReplyId(@Param("authorId") String authorId, @Param("replyId") int replyId);

    @Select("SELECT * FROM replies WHERE author_id = #{authorId} AND is_anonymous = false")
    List<Reply> findRepliesByAuthorId(@Param("authorId") String authorId);

    @Delete("DELETE FROM replies WHERE author_id = #{authorId} AND reply_id = #{replyId}")
    int deleteReply(@Param("authorId") String authorId, @Param("replyId") int replyId);

    @Select("SELECT * FROM replies WHERE post_id = #{postId}")
    List<Reply> findReplyByPostId(int postId);

    @Select("select max(reply_id) from replies")
    int findReplySize();

    @Select("select * from replies WHERE author_id = #{authorId}")
    List<Reply> findOwnFirstReply(@Param("authorId") String authorId);

    @Select("select * from replies WHERE reply_id = #{replyId}")
    Reply findReplyByReplyId(@Param("replyId") int replyId);

}
