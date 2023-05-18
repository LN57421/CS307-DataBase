package Relation.EmbededClass;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PostCategoriesId implements Serializable {
    private Integer postId;
    private Integer categoryId;

    // constructors, getters, setters...

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostCategoriesId)) return false;
        PostCategoriesId that = (PostCategoriesId) o;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, categoryId);
    }
}
