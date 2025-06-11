 package kim.donghyun.model.entity;
 
 import lombok.Data;
 
 /** 댓글 정보 */
 @Data
 public class Comment {
     private Long commentId;
     private Long postId;
     private Long userId;
    /** 작성자 이름 */
    private String username;
     private String content;
     private String createdAt;
 }
