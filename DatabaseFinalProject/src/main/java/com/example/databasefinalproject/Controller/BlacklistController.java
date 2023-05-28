package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.Blacklist;
import com.example.databasefinalproject.Mapper.BlacklistMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{authorId}/blacklist")
public class BlacklistController {

    @Autowired
    private BlacklistMapper blacklistMapper;

    @ApiOperation("将用户加入黑名单")
    @PostMapping("/add/{blockedAuthorId}")
    public ResponseEntity<Void> addToBlacklist(@PathVariable String authorId, @PathVariable  String blockedAuthorId) {
        if (blacklistMapper.addToBlacklist(authorId, blockedAuthorId) > 0) {
            return new ResponseEntity<>(HttpStatus.CREATED); // 201 创建成功
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 创建失败
        }
    }

    @ApiOperation("将用户从黑名单中移除")
    @DeleteMapping("/remove/{blockedAuthorId}")
    public ResponseEntity<Void> removeFromBlacklist(@PathVariable String authorId, @PathVariable String blockedAuthorId) {
        if (blacklistMapper.removeFromBlacklist(blockedAuthorId, authorId) > 0) {
            return new ResponseEntity<>(HttpStatus.OK); // 200 移除成功
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 移除失败
        }
    }

    @ApiOperation("获取某用户的黑名单列表")
    @GetMapping("/list")
    public List<Blacklist> getBlacklistByAuthorId(@PathVariable String authorId) {
        return blacklistMapper.getBlacklistByAuthorId(authorId);
    }


}
