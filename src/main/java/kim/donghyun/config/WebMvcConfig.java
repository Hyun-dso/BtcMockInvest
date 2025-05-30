package kim.donghyun.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AlreadySignedInterceptor()).addPathPatterns("/signin", "/signup"); // 로그인 사용자는 이 경로
																										// 접근 금지
	}
}