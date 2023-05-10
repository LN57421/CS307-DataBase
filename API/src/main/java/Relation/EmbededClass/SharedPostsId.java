package Relation.EmbededClass;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SharedPostsId implements Serializable {
    private Integer postId;
    private String sharingAuthorId;

    // constructors, getters, setters...

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SharedPostsId)) return false;
        SharedPostsId that = (SharedPostsId) o;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(sharingAuthorId, that.sharingAuthorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, sharingAuthorId);
    }
}

