package kim.donghyun.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kim.donghyun.model.entity.Comment;
import kim.donghyun.model.entity.Post;
import kim.donghyun.repository.CommentRepository;
import kim.donghyun.repository.PostRepository;
import kim.donghyun.websocket.PostWebSocketSender;
import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final PostWebSocketSender postWebSocketSender;

	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}

	public Post getPostById(long postId) {
		return postRepository.findById(postId);
	}

	public List<Comment> getCommentsByPostId(long postId) {
		return commentRepository.findByPostId(postId);
	}

	public Post createPost(Long userId, String username, String content) {
		Post post = new Post();
		post.setUserId(userId);
        post.setUsername(username);
		post.setContent(content);
		postRepository.insert(post);
		postWebSocketSender.broadcast(post);
		return post;
	}
}
