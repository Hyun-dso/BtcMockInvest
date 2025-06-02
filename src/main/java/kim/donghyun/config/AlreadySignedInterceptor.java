package kim.donghyun.config;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AlreadySignedInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 원래 코드 contextPath 경로 탐색이 안돼서 안됌
		// String uri = request.getRequestURI(); 
		String uri = request.getServletPath();

		// 이 경로들일 때만 로그인 상태일 경우 막기
		if (uri.equals("/signin") || uri.equals("/signup")) {
			HttpSession session = request.getSession(false);
			if (session != null && session.getAttribute("loginUser") != null) {
				response.sendRedirect(request.getContextPath() + "/");
//				response.sendRedirect("/");
				return false;
			}
		}

		return true;
	}
}