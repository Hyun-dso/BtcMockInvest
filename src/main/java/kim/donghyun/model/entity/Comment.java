package kim.donghyun.model.entity;

import lombok.Data;

@Data
public class Comment {
    private Long commentId;
    private Long postId;
    private Long userId;
    private String content;
    private String createdAt;
}
