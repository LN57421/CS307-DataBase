package Relation.RelationClass;

import Relation.EmbededClass.LikedPostsId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "liked_posts")
public class LikedPost {
    @EmbeddedId
    private LikedPostsId id;

    // getter and setter
}
