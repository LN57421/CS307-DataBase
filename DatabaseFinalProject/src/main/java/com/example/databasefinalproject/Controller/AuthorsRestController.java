package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.Author;
import com.example.databasefinalproject.Mapper.AuthorsMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
    public ResponseEntity<Void> createAuthors(@RequestBody Author authorNew){
        String authorName = authorNew.getAuthorName();
        String authorKey = authorNew.getAuthorKey();
        String phone = authorNew.getPhone();
        if (authorsMapper.findByName(authorName) != null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);// 409 已经存在用户
        }
        // 创建一个日期对象，表示2023年5月18日9时30分。
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, Calendar.MAY); // 注意月份从0开始，所以5表示六月
        calendar.set(Calendar.DAY_OF_MONTH, 18);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        Timestamp registrationTime = new Timestamp(date.getTime());
        String authorId = generateRandomID(18);
        while (authorsMapper.findByID(authorId) != null){
            authorId = generateRandomID(18);
        }
        Author author = new Author(authorId,authorName,authorKey,registrationTime,phone);
        if (authorsMapper.insertAuthor(author) > 0){// 返回状态码 标定是否创建成功
            return new ResponseEntity<>(HttpStatus.CREATED); // 201 创建成功
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 创建失败
        }
    }

    public String generateRandomID(int length) {
        StringBuilder idBuilder = new StringBuilder();
        String digits = "0123456789";
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(digits.length());
            char randomDigit = digits.charAt(randomIndex);
            idBuilder.append(randomDigit);
        }

        return idBuilder.toString();
    }




}
