package Entity.EntityClass;

import Entity.EntityClass.Author;
import Entity.EntityClass.Post;

import javax.persistence.*;

@Entity
@Table(name = "replies")
public class Reply {

    @Id
    @Column(name = "reply_id")
    private Integer replyId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "stars", nullable = false)
    private Integer stars;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // Getters and setters
}
