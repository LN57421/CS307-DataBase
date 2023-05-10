package Relation.RelationClass;


import Relation.EmbededClass.FollowedAuthorsId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "followed_authors")
public class FollowedAuthor {
    @EmbeddedId
    private FollowedAuthorsId id;

    // getter and setter
}
