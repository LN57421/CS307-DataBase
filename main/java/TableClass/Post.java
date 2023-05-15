package TableClass;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @Column(name = "post_id")
    private Integer postId;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "posting_time", nullable = false)
    private LocalDateTime postingTime;

    @ManyToOne
    @JoinColumn(name = "posting_city", nullable = false)
    private City city;

    // Getters and setters
}

