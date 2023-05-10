package Entity.EntityClass;

import javax.persistence.*;// 下载lib
import java.time.LocalDateTime;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @Column(name = "author_id")
    private String authorId;

    @Column(name = "author_name", unique = true, nullable = false)
    private String authorName;

    @Column(name = "registration_time", nullable = false)
    private LocalDateTime registrationTime;

    @Column(name = "phone", unique = true)
    private String phone;

    // Getters and setters
}

