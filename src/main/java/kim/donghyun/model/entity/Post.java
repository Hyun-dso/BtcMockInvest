 package kim.donghyun.model.entity;
 
 import lombok.Data;
 import java.util.List;
 
/**
+ * Simple post entity used for the community feed.
+ */
 @Data
 public class Post {
     private Long postId;
     private Long userId;
    /** 작성자 이름 */
    private String username;
     private String title;
     private String content;
     private String createdAt;
     private int likeCount;
    /** 게시글에 달린 댓글 목록 */
    private List<Comment> comments;
 }
