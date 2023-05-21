package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.*;
import com.example.databasefinalproject.Mapper.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class Homepage {

    private final AuthorsMapper authorsMapper;

    private final PostsMapper postsMapper;

    private final CityMapper cityMapper;

    private final RepliesMapper repliesMapper;

    private final SecondaryRepliesMapper secondaryRepliesMapper;

    private final FollowsMapper followsMapper;

    private final LikedPostMapper likedPostMapper;

    private final SharedPostMapper sharedPostMapper;

    private final FavoritePostMapper favoritePostMapper;

    public Homepage(AuthorsMapper authorsMapper, PostsMapper postsMapper, CityMapper cityMapper, RepliesMapper repliesMapper, SecondaryRepliesMapper secondaryRepliesMapper, FollowsMapper followsMapper, LikedPostMapper likedPostMapper, SharedPostMapper sharedPostMapper, FavoritePostMapper favoritePostMapper) {
        this.authorsMapper = authorsMapper;
        this.postsMapper = postsMapper;
        this.cityMapper = cityMapper;
        this.repliesMapper = repliesMapper;
        this.secondaryRepliesMapper = secondaryRepliesMapper;
        this.followsMapper = followsMapper;
        this.likedPostMapper = likedPostMapper;
        this.sharedPostMapper = sharedPostMapper;
        this.favoritePostMapper = favoritePostMapper;
    }

    @ApiOperation("登录")
    @PostMapping(path = "Login")
    public ResponseEntity<Object> verifyPassword(@RequestParam(required = true) String authorName,
                                               @RequestParam(required = true) String password){
        String verify = authorsMapper.verifyPassword(authorName);
        if (verify == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }else {
            if (verify.equals(password)){
                return ResponseEntity.ok(authorsMapper.getInfo(authorName));
            }else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
    }
    @ApiOperation("获取帖子")
    @GetMapping(path = "posts")
    public ResponseEntity<Object> getPosts(){
        List<Post> posts = postsMapper.getAllPost();
        for (Post temp: posts) {
            Author author = authorsMapper.findByID(temp.getAuthorId());
            City city = cityMapper.findCityByName(temp.getPostingCity());
            temp.setAuthor(author);
            temp.setCity(city);
        }
        return ResponseEntity.ok(posts);
    }

    @ApiOperation("获取单个帖子的所有具体信息")
    @GetMapping(path = "{authorId}/postContent/{id}")
    public ResponseEntity<Object> getPostInfo(@PathVariable("id") int id, @PathVariable("authorId") String authorId){
        Post post = postsMapper.findPostByPostId(id);
        post.setAuthor(authorsMapper.findByID(post.getAuthorId()));
        List<Reply> replies = repliesMapper.findReplyByPostId(post.getPostId());
        replies.forEach(reply -> {
            reply.setAuthor(authorsMapper.findByID(reply.getAuthorId()));
        });
        List<List<SecondaryReply>> secondReplyList = new ArrayList<>();
        replies.forEach(reply -> {
            List<SecondaryReply> secondaryReplies = secondaryRepliesMapper.findSecondReplyByFirstReplyId(reply.getReplyId());
            secondaryReplies.forEach(secondaryReply -> {
                secondaryReply.setAuthor(authorsMapper.findByID(secondaryReply.getAuthorId()));
            });
            secondReplyList.add(secondaryReplies);
        });
        boolean likedPost = likedPostMapper.findLikedPostsByAuthorIdAndPostName(id, authorId).size() != 0;
        boolean favoritePost = favoritePostMapper.findFavoritePostsByAuthorIdAndPostName(id, authorId).size() != 0;
        boolean sharedPost = sharedPostMapper.findRelationByAuthorIdAndPostId(authorId, id).size() != 0;
        List<Object> result = new ArrayList<>();
        result.add(post);
        result.add(replies);
        result.add(secondReplyList);
        result.add(likedPost);
        result.add(favoritePost);
        result.add(sharedPost);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("返回FavorPost")
    @GetMapping(path = "{authorId}/showFavor")
    public ResponseEntity<Object> getFavor(@PathVariable("authorId") String authorId){
        List<FavoritePost> favoritePosts = favoritePostMapper.findFavoritePostsByAuthorId(authorId);
        List<Post> posts = new ArrayList<>();
        favoritePosts.forEach(favoritePost -> {
            posts.add(postsMapper.findPostByPostId(favoritePost.getPostId()));
        });
        for (Post temp: posts) {
            Author author = authorsMapper.findByID(temp.getAuthorId());
            City city = cityMapper.findCityByName(temp.getPostingCity());
            temp.setAuthor(author);
            temp.setCity(city);
        }
        return ResponseEntity.ok(posts);
    }
    @ApiOperation("返回likePost")
    @GetMapping(path = "{authorId}/showLike")
    public ResponseEntity<Object> getLike(@PathVariable("authorId") String authorId){
        List<LikedPost> likedPosts = likedPostMapper.findLikedPostsByAuthorId(authorId);
        List<Post> posts = new ArrayList<>();
        likedPosts.forEach(likedPost -> {
            posts.add(postsMapper.findPostByPostId(likedPost.getPostId()));
        });
        for (Post temp: posts) {
            Author author = authorsMapper.findByID(temp.getAuthorId());
            City city = cityMapper.findCityByName(temp.getPostingCity());
            temp.setAuthor(author);
            temp.setCity(city);
        }
        return ResponseEntity.ok(posts);
    }
    @ApiOperation("返回sharePost")
    @GetMapping(path = "{authorId}/showShare")
    public ResponseEntity<Object> getShare(@PathVariable("authorId") String authorId){
        List<SharedPost> sharedPosts = sharedPostMapper.findSharedPostsByAuthorId(authorId);
        List<Post> posts = new ArrayList<>();
        sharedPosts.forEach(sharedPost -> {
            posts.add(postsMapper.findPostByPostId(sharedPost.getPostId()));
        });
        for (Post temp: posts) {
            Author author = authorsMapper.findByID(temp.getAuthorId());
            City city = cityMapper.findCityByName(temp.getPostingCity());
            temp.setAuthor(author);
            temp.setCity(city);
        }
        return ResponseEntity.ok(posts);
    }
}
