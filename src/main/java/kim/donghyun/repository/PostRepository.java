package kim.donghyun.repository;

import kim.donghyun.model.entity.Post;
import java.util.List;

public interface PostRepository {
    void insert(Post post);
    List<Post> findAll();
    Post findById(Long postId);
    List<Post> findByUserId(Long userId);
}
