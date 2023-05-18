package Relation.RelationClass;

import Relation.EmbededClass.SharedPostsId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "shared_posts")
public class SharedPost {
    @EmbeddedId
    private SharedPostsId id;

    // getter and setter
}
