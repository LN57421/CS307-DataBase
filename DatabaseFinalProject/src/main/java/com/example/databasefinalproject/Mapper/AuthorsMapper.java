package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.Author;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AuthorsMapper {

    @Select("select * from authors")
    List<Author> findAll();


}
