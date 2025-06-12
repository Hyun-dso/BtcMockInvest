package kim.donghyun.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import kim.donghyun.model.entity.Post;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostWebSocketSender {

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcast(Post post) {
        messagingTemplate.convertAndSend("/topic/posts", post);
    }
}