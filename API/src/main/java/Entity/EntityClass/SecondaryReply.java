package Entity.EntityClass;

import Entity.EntityClass.Author;
import Entity.EntityClass.Reply;

import javax.persistence.*;

@Entity
@Table(name = "secondary_replies")
public class SecondaryReply {

    @Id
    @Column(name = "secondary_reply_id")
    private Integer secondaryReplyId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "stars", nullable = false)
    private Integer stars;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "reply_id", nullable = false)
    private Reply reply;

    // Getters and setters
}

