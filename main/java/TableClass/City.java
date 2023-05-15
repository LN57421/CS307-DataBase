package TableClass;

import javax.persistence.*;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @Column(name = "city_name", nullable = false)
    private String cityName;

    @Column(name = "city_state", nullable = false)
    private String cityState;

    // Getters and setters
}
