package Entity.Controller;

import Entity.EntityClass.City;
import Entity.ServiceClass.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/cities")
public class PostingCityController {

    private final CityService postingCityService;

    @Autowired
    public PostingCityController(CityService postingCityService) {
        this.postingCityService = postingCityService;
    }

    @PostMapping
    public City addCity(@RequestBody City postingCity) {
        return postingCityService.addCity(postingCity);
    }

    @GetMapping
    public List<City> getAllCities() {
        return postingCityService.findAllCities();
    }

    @PutMapping
    public City updateCity(@RequestBody City postingCity) {
        return postingCityService.updateCity(postingCity);
    }

    @DeleteMapping(path = "{cityName}")
    public void deleteCity(@PathVariable("cityName") String cityName) {
        postingCityService.deleteCity(cityName);
    }
}

