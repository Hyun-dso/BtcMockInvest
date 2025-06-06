package kim.donghyun.websocket;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kim.donghyun.repository.TradeExecutionRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VolumeBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;
    private final TradeExecutionRepository tradeExecutionRepository;

    @Scheduled(fixedDelay = 1000)
    public void broadcastVolume() {
        LocalDateTime since = LocalDateTime.now().minusSeconds(1);
        BigDecimal volume = tradeExecutionRepository.findVolumeSince(since);
        Map<String, Object> payload = new HashMap<>();
        payload.put("volume", volume);
        messagingTemplate.convertAndSend("/topic/volume", payload);
    }
}