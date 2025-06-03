package snippet;

public class Snippet {
	# RestTemplate 로그 줄이기
	logging.level.org.springframework.web.client.RestTemplate=OFF
	
	# WebSocket 브로커 로그 줄이기
	logging.level.org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler=OFF
	
	# JDBC 연결 로그 줄이기
	logging.level.org.springframework.jdbc.datasource.DataSourceUtils=OFF
}

