package Relation.EmbededClass;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LikedPostsId implements Serializable {
    private Integer postId;
    private String likingAuthorId;

    // constructors, getters, setters...

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LikedPostsId)) return false;
        LikedPostsId that = (LikedPostsId) o;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(likingAuthorId, that.likingAuthorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, likingAuthorId);
    }
}

