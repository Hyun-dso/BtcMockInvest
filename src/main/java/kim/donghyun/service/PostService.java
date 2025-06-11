 package kim.donghyun.service;
 
import kim.donghyun.model.entity.Comment;
import kim.donghyun.model.entity.Post;
 import org.springframework.stereotype.Service;
 
 import java.util.*;
 
 @Service
 public class PostService {
 
    private final List<Post> posts = new ArrayList<>();
    private final Map<Long, List<Comment>> commentMap = new HashMap<>();
 
    // 간단한 사용자 ID -> 이름 매핑
    private final Map<Long, String> userMap = new HashMap<>();
 
    public PostService() {
       

        // 초기 댓글 세팅
        posts.forEach(p -> p.setComments(commentMap.get(p.getPostId())));
    }

    public List<Post> getAllPosts() {
        posts.forEach(p -> p.setComments(commentMap.get(p.getPostId())));
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
   
 