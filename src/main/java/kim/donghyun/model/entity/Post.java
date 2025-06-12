package kim.donghyun.model.entity;

import lombok.Data;

@Data
public class Post {
    private Long postId;
    private Long userId;
    private String username;
    private String content;
    private String createdAt;
}
