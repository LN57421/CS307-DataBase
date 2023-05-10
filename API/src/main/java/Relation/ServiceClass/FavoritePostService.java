package Relation.ServiceClass;

import Relation.EmbededClass.FavoritePostsId;
import Relation.RelationClass.FavoritePost;
import Relation.Repository.FavoritePostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritePostService {
    private final FavoritePostsRepository favoritePostRepository;

    @Autowired
    public FavoritePostService(FavoritePostsRepository favoritePostRepository) {
        this.favoritePostRepository = favoritePostRepository;
    }

    public FavoritePost addFavoritePost(FavoritePost favoritePost) {
        return favoritePostRepository.save(favoritePost);
    }

    public List<FavoritePost> findAllFavoritePosts() {
        return favoritePostRepository.findAll();
    }

    public FavoritePost updateFavoritePost(FavoritePost favoritePost) {
        return favoritePostRepository.save(favoritePost);
    }

    public void deleteFavoritePost(FavoritePostsId id) {
        favoritePostRepository.deleteById(id);
    }
}

