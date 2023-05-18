package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.Author;
import com.example.databasefinalproject.Mapper.AuthorsMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/Authors")
public class AuthorsRestController {

    @Autowired
    private AuthorsMapper authorsMapper;

    @ApiOperation("获取所有authors")
    @GetMapping("name")
    public List<Author> getAllAuthors(){
        return authorsMapper.findAll();
    }

    @ApiOperation("创建新的author")
    @PostMapping("create")
    public ResponseEntity<Void> createAuthors(Author author){
        if (authorsMapper.insertAuthor(author) > 0){// 返回状态码 标定是否创建成功
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
