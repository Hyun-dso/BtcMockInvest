package kim.donghyun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import kim.donghyun.model.dto.SigninRequest;
import kim.donghyun.model.dto.SignupRequest;
import kim.donghyun.model.entity.User;
import kim.donghyun.service.AuthService;

@Controller
public class AuthController {

	@Autowired
	private AuthService authService;

	@GetMapping("/signup")
	public String signupForm() {
		return "signup";
	}

	@PostMapping("/signup")
	public String signup(@ModelAttribute SignupRequest req, Model model) {
		String result = authService.signup(req);
		if (!"success".equals(result)) {
			model.addAttribute("error", result); // "email" 또는 "username"
			return "signup";
		}
		return "redirect:/signin";
	}

	@GetMapping("/signin")
	public String signinForm() {
		return "signin";
	}

	@PostMapping("/signin")
	public String signin(@ModelAttribute SigninRequest req, HttpSession session, Model model) {
		User user = authService.signin(req);
		if (user == null) {
			model.addAttribute("error", "이메일 또는 비밀번호가 올바르지 않거나 인증되지 않았습니다.");
			return "signin";
		}

		session.setAttribute("loginUser", user); // 세션에 사용자 저장
		return "redirect:/"; // 로그인 성공 시 메인으로 이동
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // 세션 초기화
		return "redirect:/";
	}
}
