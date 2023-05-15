
package EmbededClass;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class FavoritePostsId implements Serializable {
    private Integer postId;
    private String favoriteAuthorId;

    // constructors, getters, setters...

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoritePostsId)) return false;
        FavoritePostsId that = (FavoritePostsId) o;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(favoriteAuthorId, that.favoriteAuthorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, favoriteAuthorId);
    }

    public Integer getPostId() {
        return postId;
    }

    public String getFavoriteAuthorId() {
        return favoriteAuthorId;
    }
}
