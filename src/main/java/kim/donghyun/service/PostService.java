package kim.donghyun.service;

import kim.donghyun.model.entity.Post;
import kim.donghyun.model.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    // 게시글 ID로 게시글 정보를 가져오기
    public Post getPostById(int postId) {
        Post post = new Post();
        post.setPostId((long) postId);
        post.setUserId(1L);
        post.setTitle("예시 제목");
        post.setContent("예시 내용");
        post.setCreatedAt("2024-01-01");
        return post;
    }

    // 게시글 ID로 해당 게시글의 댓글 목록 가져오기
    public List<Comment> getCommentsByPostId(int postId) {
        List<Comment> comments = new ArrayList<>();

        Comment comment1 = new Comment();
        comment1.setCommentId(1L);
        comment1.setPostId((long) postId);
        comment1.setUserId(1L);
        comment1.setContent("첫 번째 댓글");
        comment1.setCreatedAt("2024-01-01");

        Comment comment2 = new Comment();
        comment2.setCommentId(2L);
        comment2.setPostId((long) postId);
        comment2.setUserId(2L);
        comment2.setContent("두 번째 댓글");
        comment2.setCreatedAt("2024-01-02");

        comments.add(comment1);
        comments.add(comment2);

        return comments;
    }
}
