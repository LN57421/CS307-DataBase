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
    List<Post> findAllPosts();

    // 得到所有帖子
    @Select("select * from posts")
    List<Post> getAllPost();

    @Select("select * from posts where post_id = #{postId}")
    Post findPostByPostId(int postId);


    //返回热门帖子（点赞数最高的十个帖子）
    @Select("select * from posts where post_id in (SELECT posts.post_id FROM posts JOIN liked_posts ON posts.post_id = liked_posts.post_id GROUP BY posts.post_id ORDER BY COUNT(liked_posts.liking_author_id) DESC LIMIT 10);")
    List<Post> findHostPost();

//    @Select("select * from posts where post_id in (SELECT posts.post_id FROM posts JOIN liked_posts ON posts.post_id = liked_posts.post_id GROUP BY posts.post_id ORDER BY COUNT(liked_posts.liking_author_id) DESC LIMIT 10);")
//    List<Integer> findHostPost();

    // 按时间段搜索帖子
    @Select("select * from posts where posting_time >= #{begin} and posting_time <= #{end}")
    List<Post> findPostByTimeInterval(Timestamp begin, Timestamp end);
    // 按文章标题关键词词进行搜索
    @Select("select * from posts where title like '%#{KeyWord}%'")
    List<Post> findPostByKeyWordInTitle(String keyWord);

    // 按文章内容关键词进行搜索
    @Select("select * from posts where content like '%#{KeyWord}%'")
    List<Post> findPostByKeyWordInContent(String keyWord);

    // 按类型搜索帖子
    @Select("select * from posts where post_id in (SELECT posts.post_id FROM posts JOIN post_categories ON posts.post_id = post_categories.post_id JOIN" +
            "    categories ON post_categories.category_id = categories.category_id" +
            "            WHERE" +
            "    categories.category_name LIKE '%#{category_name}%')")
    List<Post> findPostByCategory(String category_name);

}
