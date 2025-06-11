package kim.donghyun.service;

import kim.donghyun.model.entity.Comment; 
import kim.donghyun.model.entity.Post; 
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {

	 private final List<Post> posts = new ArrayList<>();
	    private final Map<Long, List<Comment>> commentMap = new HashMap<>();

	    public PostService() {
	        // 첫 번째 게시글 및 댓글
	        Post p1 = new Post();
	        p1.setPostId(1L);
	        p1.setUserId(1L);
	        p1.setTitle("오늘의 BTC 전망");
	        p1.setContent("BTC가 오늘 상승 흐름을 타고 있습니다. 여러분의 생각은?");
	        p1.setLikeCount(3);
	        p1.setCreatedAt("2024-01-01");
	        posts.add(p1);

	        List<Comment> comments1 = new ArrayList<>();
	        Comment c1 = new Comment();
	        c1.setCommentId(1L);
	        c1.setPostId(1L);
	        c1.setUserId(1L);
	        c1.setContent("저도 동의합니다!");
	        c1.setCreatedAt("2024-01-01");
	        comments1.add(c1);

	        Comment c2 = new Comment();
	        c2.setCommentId(2L);
	        c2.setPostId(1L);
	        c2.setUserId(2L);
	        c2.setContent("조심해야 할 타이밍 같아요");
	        c2.setCreatedAt("2024-01-01");
	        comments1.add(c2);
	        commentMap.put(1L, comments1);

	        // 두 번째 게시글 및 댓글
	        Post p2 = new Post();
	        p2.setPostId(2L);
	        p2.setUserId(2L);
	        p2.setTitle("신규 진입 시점인가요?");
	        p2.setContent("지금 매수 타이밍인지 고민되네요. 조언 부탁드립니다!");
	        p2.setLikeCount(1);
	        p2.setCreatedAt("2024-01-02");
	        posts.add(p2);

	        List<Comment> comments2 = new ArrayList<>();
	        Comment c3 = new Comment();
	        c3.setCommentId(3L);
	        c3.setPostId(2L);
	        c3.setUserId(3L);
	        c3.setContent("아직은 기다리는게 좋아보여요");
	        c3.setCreatedAt("2024-01-02");
	        comments2.add(c3);
	        commentMap.put(2L, comments2);
	    }

	    public List<Post> getAllPosts() {
	        return posts;
	    }
	    
    public Post getPostById(int postId) {
    	 return posts.stream()
                 .filter(p -> p.getPostId() == postId)
                 .findFirst()
                 .orElse(null);
     }
    
    public List<Comment> getCommentsByPostId(int postId) {
        return commentMap.getOrDefault((long) postId, Collections.emptyList());
    }
}
  
