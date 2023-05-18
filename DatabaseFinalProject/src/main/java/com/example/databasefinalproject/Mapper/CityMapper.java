package com.example.databasefinalproject.Mapper;

import com.example.databasefinalproject.Entity.City;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CityMapper {
    @Select("select * from cities where city_name = #{cityName}")
    City findCityByName(String cityName);

    @Insert("insert into cities(city_name, city_state) values(#{cityName}, #{cityState})")
    int createCity( @Param("cityName")String cityName,
                    @Param("cityState")String cityState);

    @Delete("delete from cities where city_name = #{cityName}")
    int deleteCity(String cityName);

    @Select("select * from cities")
    List<City> findAllCities();
}