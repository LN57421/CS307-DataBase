package TableClass;

import EmbededClass.LikedPostsId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "liked_posts")
public class LikedPost {
    @EmbeddedId
    private LikedPostsId id;

    // getter and setter

    public LikedPostsId getId() {
        return id;
    }
}
