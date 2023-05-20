package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.Author;
import com.example.databasefinalproject.Entity.City;
import com.example.databasefinalproject.Entity.Post;
import com.example.databasefinalproject.Mapper.AuthorsMapper;
import com.example.databasefinalproject.Mapper.CityMapper;
import com.example.databasefinalproject.Mapper.PostsMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class Homepage {

    private final AuthorsMapper authorsMapper;

    private final PostsMapper postsMapper;

    private final CityMapper cityMapper;

    public Homepage(AuthorsMapper authorsMapper, PostsMapper postsMapper, CityMapper cityMapper) {
        this.authorsMapper = authorsMapper;
        this.postsMapper = postsMapper;
        this.cityMapper = cityMapper;
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
}
