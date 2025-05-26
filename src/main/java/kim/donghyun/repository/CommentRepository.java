package kim.donghyun.repository;

import kim.donghyun.model.entity.Comment;
import java.util.List;

public interface CommentRepository {
    void insert(Comment comment);
    List<Comment> findByPostId(Long postId);
    List<Comment> findByUserId(Long userId);
}
