package kim.donghyun.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws-endpoint").setAllowedOriginPatterns("*").withSockJS(); // 클라이언트 접속
																											// 경로
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic"); // 구독 경로
		config.setApplicationDestinationPrefixes("/app"); // 메시지 보낼 때 prefix
	}

}

// 프론트 시세 불러오기
//const socket = new SockJS("/ws-endpoint");
//const client = Stomp.over(socket);
//
//client.connect({}, () => {
//    client.subscribe("/topic/price", (message) => {
//        const price = message.body;
//        document.getElementById("btc-price").innerText = price;
//    });
//});
