package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.*;
import com.example.databasefinalproject.Mapper.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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

    private final PostCategoryMapper postCategoryMapper;

    private final CategoryMapper categoryMapper;

    private final BlacklistMapper blacklistMapper;
    public Homepage(AuthorsMapper authorsMapper, PostsMapper postsMapper, CityMapper cityMapper, RepliesMapper repliesMapper,
                    SecondaryRepliesMapper secondaryRepliesMapper, FollowsMapper followsMapper,
                    LikedPostMapper likedPostMapper, SharedPostMapper sharedPostMapper,
                    FavoritePostMapper favoritePostMapper, PostCategoryMapper postCategoryMapper,
                    CategoryMapper categoryMapper, BlacklistMapper blacklistMapper) {
        this.authorsMapper = authorsMapper;
        this.postsMapper = postsMapper;
        this.cityMapper = cityMapper;
        this.repliesMapper = repliesMapper;
        this.secondaryRepliesMapper = secondaryRepliesMapper;
        this.followsMapper = followsMapper;
        this.likedPostMapper = likedPostMapper;
        this.sharedPostMapper = sharedPostMapper;
        this.favoritePostMapper = favoritePostMapper;
        this.postCategoryMapper = postCategoryMapper;
        this.categoryMapper = categoryMapper;
        this.blacklistMapper = blacklistMapper;
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
    @GetMapping(path = "{authorId}/AllPosts/{login}")
    public ResponseEntity<Object> getPosts(@PathVariable("authorId") String authorId, @PathVariable("login") boolean login){
        List<Post> posts = postsMapper.getAllPost();
        for (Post temp: posts) {
            Author author = authorsMapper.findByID(temp.getAuthorId());
            City city = cityMapper.findCityByName(temp.getPostingCity());
            temp.setAuthor(author);
            temp.setCity(city);
        }
        if (login){
            List<Blacklist> blacklists = blacklistMapper.getBlacklistByAuthorId(authorId);

            if (blacklists.size() != 0){
                for (Blacklist blacklist: blacklists) {
                    posts.removeIf(temp -> Objects.equals(temp.getAuthorId(), blacklist.getBlockedAuthorId()));
                }
            }
        }
        return ResponseEntity.ok(posts);
    }

    @ApiOperation("获取热搜帖子")
    @GetMapping(path = "{authorId}/HostPosts/{login}")
    public ResponseEntity<Object> getHostPosts(@PathVariable("authorId") String authorId, @PathVariable("login") boolean login){
        List<Post> posts = postsMapper.findHostPost();
        for (Post temp: posts) {
            Author author = authorsMapper.findByID(temp.getAuthorId());
            City city = cityMapper.findCityByName(temp.getPostingCity());
            temp.setAuthor(author);
            temp.setCity(city);
        }
        if (login){
            List<Blacklist> blacklists = blacklistMapper.getBlacklistByAuthorId(authorId);

            if (blacklists.size() != 0){
                for (Blacklist blacklist: blacklists) {
                    posts.removeIf(temp -> Objects.equals(temp.getAuthorId(), blacklist.getBlockedAuthorId()));
                }
            }
        }
        return ResponseEntity.ok(posts);
    }

    @ApiOperation("根据时间搜索帖子")
    @GetMapping(path = "{authorId}/PostsByTime/{login}")
    public ResponseEntity<Object> getPostsByTimeInterval(@RequestParam(required = true) Timestamp startTime,
                                                         @RequestParam(required = true) Timestamp endTime,
                                                         @PathVariable("authorId") String authorId,
                                                         @PathVariable("login") boolean login){
        List<Post> posts = postsMapper.findPostByTimeInterval(startTime, endTime);
        for (Post temp: posts) {
            Author author = authorsMapper.findByID(temp.getAuthorId());
            City city = cityMapper.findCityByName(temp.getPostingCity());
            temp.setAuthor(author);
            temp.setCity(city);
        }
        if (login){
            List<Blacklist> blacklists = blacklistMapper.getBlacklistByAuthorId(authorId);

            if (blacklists.size() != 0){
                for (Blacklist blacklist: blacklists) {
                    posts.removeIf(temp -> Objects.equals(temp.getAuthorId(), blacklist.getBlockedAuthorId()));
                }
            }
        }
        return ResponseEntity.ok(posts);
    }

    @ApiOperation("按文章标题关键词词进行搜索")
    @GetMapping(path = "{authorId}/PostsByKeyWordInTitles/{login}")
    public ResponseEntity<Object> getPostsByKeyWordInTitles(@RequestParam(required = true) String keyWords,
                                                             @PathVariable("authorId") String authorId,
                                                            @PathVariable("login") boolean login){
        List<Post> posts = postsMapper.findPostByKeyWordInTitle(keyWords);
        for (Post temp: posts) {
            Author author = authorsMapper.findByID(temp.getAuthorId());
            City city = cityMapper.findCityByName(temp.getPostingCity());
            temp.setAuthor(author);
            temp.setCity(city);
        }
        if (login){
            List<Blacklist> blacklists = blacklistMapper.getBlacklistByAuthorId(authorId);

            if (blacklists.size() != 0){
                for (Blacklist blacklist: blacklists) {
                    posts.removeIf(temp -> Objects.equals(temp.getAuthorId(), blacklist.getBlockedAuthorId()));
                }
            }
        }
        return ResponseEntity.ok(posts);
    }

    @ApiOperation("按文章内容关键词词进行搜索")
    @GetMapping(path = "{authorId}/PostsByKeyWordInContents/{login}")
    public ResponseEntity<Object> getPostsByKeyWordInContent(@RequestParam(required = true) String keyWords,
                                                             @PathVariable("authorId") String authorId,
                                                             @PathVariable("login") boolean login){
        List<Post> posts = postsMapper.findPostByKeyWordInContent(keyWords);
        for (Post temp: posts) {
            Author author = authorsMapper.findByID(temp.getAuthorId());
            City city = cityMapper.findCityByName(temp.getPostingCity());
            temp.setAuthor(author);
            temp.setCity(city);
        }
        if (login){
            List<Blacklist> blacklists = blacklistMapper.getBlacklistByAuthorId(authorId);

            if (blacklists.size() != 0){
                for (Blacklist blacklist: blacklists) {
                    posts.removeIf(temp -> Objects.equals(temp.getAuthorId(), blacklist.getBlockedAuthorId()));
                }
            }
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
//        List<PostCategory> categories = postCategoryMapper.findPostCategoriesByPostId(post.getPostId());
//        List<String> categories_result = new ArrayList<>();
//        for (PostCategory p: categories) {
//            categories_result.add(categoryMapper.getCategoryById(p.getPostId()).getCategoryName());
//        }
//        String[] re = new String[20];
//        categories_result.toArray(re); // re 数组中储存了很多category 是一个String类型的数组
//        result.add(re);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("返回FavorPost")
    @GetMapping(path = "{authorId}/showFavor/{login}")
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
    @GetMapping(path = "{authorId}/showLike/{login}")
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

    @ApiOperation("获取单个author的信息")
    @GetMapping(path = "{authorId}/getFollow/{followId}")
    public ResponseEntity<Object> getPostAuthor(@PathVariable("authorId") String authorId, @PathVariable("followId") String followId){
        boolean isFollow = followsMapper.findFollowedByAuthorIdAndFollowerId(authorId, followId).size() != 0;
        return ResponseEntity.ok(isFollow);
    }

    @ApiOperation("获取作者关注列表")
    @GetMapping(path = "{authorId}/getFollowList")
    public ResponseEntity<Object> getFollowList(@PathVariable("authorId") String authorId){
        List<FollowedAuthor> followedAuthors = followsMapper.findFollowingByAuthorId(authorId);
        List<Author> authors = new ArrayList<>();
        followedAuthors.forEach(followedAuthor -> {
            authors.add(authorsMapper.findByID(followedAuthor.getAuthorId()));
        });
        boolean[] isFollow = new boolean[followedAuthors.size()];
        for (int i = 0; i < followedAuthors.size(); i++) {
            isFollow[i] = true;
        }
        List<Object> result = new ArrayList<>();
        result.add(authors);
        result.add(isFollow);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("获取个人创建信息")
    @GetMapping(path = "{authorId}/showPost")
    public ResponseEntity<Object> showPost(@PathVariable("authorId") String authorId){
        List<Post> createPosts = postsMapper.findPostsByAuthorId(authorId);
        Author author = authorsMapper.findByID(authorId);
        createPosts.forEach(post -> {
            City city = cityMapper.findCityByName(post.getPostingCity());
            post.setAuthor(author);
            post.setCity(city);
        });
        return ResponseEntity.ok(createPosts);
    }


}
