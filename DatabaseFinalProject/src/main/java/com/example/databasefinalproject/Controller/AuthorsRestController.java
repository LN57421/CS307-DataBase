package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.Authors;
import com.example.databasefinalproject.Mapper.AuthorsMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/Authors")
public class AuthorsRestController {

    @Autowired
    private AuthorsMapper authorsMapper;

    @ApiOperation("获取所有authors")
    @GetMapping
    public List<Authors> getAllAuthors(){
        return authorsMapper.findAll();
    }

}
