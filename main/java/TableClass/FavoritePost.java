package TableClass;

import EmbededClass.FavoritePostsId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "favorite_posts")
public class FavoritePost {
    @EmbeddedId
    private FavoritePostsId id;

    // getter and setter

    public FavoritePostsId getId() {
        return id;
    }

    public void setId(FavoritePostsId id) {
        this.id = id;
    }
}
