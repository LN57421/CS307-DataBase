package Relation.ServiceClass;


import Relation.EmbededClass.FollowedAuthorsId;
import Relation.RelationClass.FollowedAuthor;
import Relation.Repository.FollowedAuthorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowedAuthorService {
    private final FollowedAuthorsRepository followedAuthorRepository;

    @Autowired
    public FollowedAuthorService(FollowedAuthorsRepository followedAuthorRepository) {
        this.followedAuthorRepository = followedAuthorRepository;
    }

    public FollowedAuthor followAuthor(FollowedAuthor followedAuthor) {
        return followedAuthorRepository.save(followedAuthor);
    }

    public List<FollowedAuthor> findAllFollowedAuthors() {
        return followedAuthorRepository.findAll();
    }

    public FollowedAuthor updateFollowedAuthor(FollowedAuthor followedAuthor) {
        return followedAuthorRepository.save(followedAuthor);
    }

    public void unfollowAuthor(FollowedAuthorsId id) {
        followedAuthorRepository.deleteById(id);
    }
}

