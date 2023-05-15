package ServiceClass;

import EmbededClass.FavoritePostsId;
import TableClass.FavoritePost;
import Repository.FavoritePostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FavoritePostService {
    private final FavoritePostsRepository favoritePostRepository;

    @Autowired
    public FavoritePostService(FavoritePostsRepository favoritePostRepository) {
        this.favoritePostRepository = favoritePostRepository;
    }

    public FavoritePost addFavoritePost(FavoritePost favoritePost) {
        // 检查用户是否已经收藏过这个帖子
        FavoritePost existingFavoritePost = favoritePostRepository.findByUserIdAndPostId(favoritePost.getId().getFavoriteAuthorId(), favoritePost.getId().getPostId());
        if (existingFavoritePost != null) {
            throw new DBException("用户已经收藏过这个帖子");
        }
        return favoritePostRepository.save(favoritePost);
    }

    public List<FavoritePost> getFavoritePostsByUserId(Long userId) {
        return Optional.ofNullable(favoritePostRepository.findByUserId(userId))
                .orElse(Collections.emptyList());
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

