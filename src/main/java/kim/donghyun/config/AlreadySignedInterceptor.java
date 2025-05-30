package kim.donghyun.config;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AlreadySignedInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String uri = request.getRequestURI();

		// 이 경로들일 때만 로그인 상태일 경우 막기
		if (uri.equals("/signin") || uri.equals("/signup")) {
			HttpSession session = request.getSession(false);
			if (session != null && session.getAttribute("loginUser") != null) {
				response.sendRedirect("/");
				return false;
			}
		}

		return true;
	}
}