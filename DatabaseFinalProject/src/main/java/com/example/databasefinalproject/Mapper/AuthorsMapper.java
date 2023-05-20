package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.Author;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AuthorsMapper {

    @Select("select * from authors where author_name = #{authorName}")
    Author getInfo(String authorName);

    @Select("select author_key from authors where author_name = #{authorName}")
    String verifyPassword(String authorName);

    @Select("select * from authors where author_name = #{authorName}")
    Author findByName(String authorName);

    @Select("select * from authors where author_id = #{authorId}")
    Author findByID(String authorId);

    @Select("select * from authors")
    List<Author> findAll();

    @Insert("insert into authors(author_id, author_name, author_key, registration_time, phone) " +
            "values(#{authorId}, #{authorName},  #{authorKey}, #{registrationTime},  #{phone})")
    int insertAuthor(Author author);


}
