package kim.donghyun.service;

import kim.donghyun.model.entity.User;
import java.util.List; 
import kim.donghyun.repository.UserRepository;
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
	private final UserRepository userRepository; // ← 꼭 있어야 userId로 유저 조회 가능
	
	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}

	public Post getPostById(long postId) {
		return postRepository.findById(postId);
	}

	public List<Comment> getCommentsByPostId(long postId) {
		return commentRepository.findByPostId(postId);
	}

	public Post createPost(Long userId, String title, String content) {
		User user = userRepository.findById(userId);
		if (user == null) {
		    throw new RuntimeException("user not found for id = " + userId);
		}

		
		Post post = new Post();
		post.setUserId(userId);
		post.setTitle(title);
		post.setContent(content);
		post.setUsername(user.getUsername()); // ✅ 작성자 이름 세팅
		postRepository.insert(post);
		postWebSocketSender.broadcast(post);
		return post;
	}
} 
