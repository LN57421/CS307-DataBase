package Relation.RelationClass;

import Relation.EmbededClass.FavoritePostsId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "favorite_posts")
public class FavoritePost {
    @EmbeddedId
    private FavoritePostsId id;

    // getter and setter
}
