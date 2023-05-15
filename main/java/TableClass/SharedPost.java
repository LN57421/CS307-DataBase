package TableClass;

import EmbededClass.SharedPostsId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "shared_posts")
public class SharedPost {
    @EmbeddedId
    private SharedPostsId id;

    // getter and setter


    public SharedPostsId getId() {
        return id;
    }
}
