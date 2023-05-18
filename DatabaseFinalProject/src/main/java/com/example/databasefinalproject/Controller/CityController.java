package com.example.databasefinalproject.Controller;

import com.example.databasefinalproject.Entity.City;
import com.example.databasefinalproject.Mapper.CityMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {
    @Autowired
    private CityMapper citiesMapper;

    @ApiOperation("创建一个新的城市")
    @PostMapping("/create")
    public ResponseEntity<Void> createCity(String cityName, String cityState) {
        if (citiesMapper.findCityByName(cityName) != null){
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409 已存在该城市
        }
        if (citiesMapper.createCity(cityName, cityState) > 0) {
            return new ResponseEntity<>(HttpStatus.CREATED); // 201 创建成功
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 创建失败
        }
    }

    @ApiOperation("删除一个城市")
    @DeleteMapping("/delete/{cityName}")
    public ResponseEntity<Void> deleteCity(@PathVariable String cityName) {
        if (citiesMapper.findCityByName(cityName) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 没有找到该城市
        }
        if (citiesMapper.deleteCity(cityName) > 0) {
            return new ResponseEntity<>(HttpStatus.OK); // 200 删除成功
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 删除失败
        }
    }

    @ApiOperation("获取所有城市")
    @GetMapping("/show")
    public ResponseEntity<List<City>> findAllCities() {
        List<City> cities = citiesMapper.findAllCities();
        if (cities.isEmpty()) {
            return ResponseEntity.notFound().build(); // 返回404 Not Found
        } else {
            return ResponseEntity.ok(cities); // 返回城市列表
        }
    }
}
