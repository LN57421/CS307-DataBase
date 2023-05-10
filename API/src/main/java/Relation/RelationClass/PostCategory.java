package Relation.RelationClass;



import Relation.EmbededClass.PostCategoriesId;

import javax.persistence.*;


@Entity
@Table(name = "post_categories")
public class PostCategory {
    @EmbeddedId
    private PostCategoriesId id;

    // getter and setter
}






