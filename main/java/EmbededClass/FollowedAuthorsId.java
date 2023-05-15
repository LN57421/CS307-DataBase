package EmbededClass;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FollowedAuthorsId implements Serializable {
    private String authorId;
    private String followerAuthorId;

    public FollowedAuthorsId() {}

    public FollowedAuthorsId(String authorId, String followerAuthorId) {
        this.authorId = authorId;
        this.followerAuthorId = followerAuthorId;
    }

    // constructors, getters, setters...

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowedAuthorsId)) return false;
        FollowedAuthorsId that = (FollowedAuthorsId) o;
        return Objects.equals(authorId, that.authorId) &&
                Objects.equals(followerAuthorId, that.followerAuthorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId, followerAuthorId);
    }
}

