package kim.donghyun.controller;

import kim.donghyun.model.dto.SignupRequest;
import kim.donghyun.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
		boolean success = authService.signup(req);
		if (!success) {
			model.addAttribute("error", "이미 사용 중인 이메일입니다.");
			return "signup";
		}
		return "redirect:/signin";
	}
}
