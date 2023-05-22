package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.Post;
import com.example.databasefinalproject.Mapper.CityMapper;
import com.example.databasefinalproject.Mapper.PostsMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/{authorId}/posts")
public class PostsController {
    @Autowired
    private PostsMapper postsMapper;
    @Autowired
    private CityMapper cityMapper;

    @ApiOperation("创建新的帖子")
    @PostMapping("/create")
    public ResponseEntity<Void> createPost(@PathVariable("authorId") String authorId,
                                           String title,
                                           String content,
                                           String city,
                                           String state) {
        int postId = postsMapper.findAllPosts().size() + 1;
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
        if (postsMapper.createPost(postId, authorId, title, content, registrationTime,city) > 0){
            if (cityMapper.findCityByName(city) == null) {
                cityMapper.createCity(city, state); // 如果城市不存在，先添加城市
            }
            return new ResponseEntity<>(HttpStatus.CREATED);//201
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);//500
        }
    }


    @ApiOperation("查找用户创建的所有帖子")
    @GetMapping("/show")
    public List<Post> findPostsByAuthorId(@PathVariable String authorId) {
        return postsMapper.findPostsByAuthorId(authorId);
    }
}
