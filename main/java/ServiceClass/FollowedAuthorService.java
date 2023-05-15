package ServiceClass;

import EmbededClass.FollowedAuthorsId;
import Repository.FollowedAuthorsRepository;
import TableClass.FollowedAuthor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FollowedAuthorService {
    private final FollowedAuthorsRepository followedAuthorRepository;

    @Autowired
    public FollowedAuthorService(FollowedAuthorsRepository followedAuthorRepository) {
        this.followedAuthorRepository = followedAuthorRepository;
    }

    public String followAuthor(FollowedAuthor followedAuthor) {
        if (followedAuthorRepository.existsById(followedAuthor.getId())) {
            return "You have already followed this author.";
        }
        followedAuthorRepository.save(followedAuthor);
        return "Successfully followed the author.";
    }

    public String unfollowAuthor(FollowedAuthorsId id) {
        if (!followedAuthorRepository.existsById(id)) {
            return "You are not following this author.";
        }
        followedAuthorRepository.deleteById(id);
        return "Successfully unfollowed the author.";
    }

    public List<FollowedAuthor> findFollowedByAuthor(String userId) {
        return Optional.ofNullable(followedAuthorRepository.findAllByAuthorID(userId))
                .orElse(Collections.emptyList());

    }
    public List<FollowedAuthor> findAllFollowedAuthors() {
        return followedAuthorRepository.findAll();
    }

    public FollowedAuthor updateFollowedAuthor(FollowedAuthor followedAuthor) {
        return followedAuthorRepository.save(followedAuthor);
    }


}

