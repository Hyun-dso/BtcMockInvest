package kim.donghyun.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kim.donghyun.model.entity.TradeExecution;
import kim.donghyun.model.entity.User;
import kim.donghyun.service.TradeHistoryService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final TradeHistoryService tradeHistoryService;

	@RequestMapping("/")
	public String index() {
		return "main"; // 처음 진입시 보여줄 화면
	}

	@RequestMapping("/main")
	public String main() {
		return "main";
	}

	@RequestMapping("/btc")
	public String home() {
		return "btc";
	}

    @RequestMapping("/mypage")
    public String mypage() {
        return "mypage";  // /WEB-INF/views/mypage.jsp
    }
    
    @RequestMapping("/mywallet")
    public String mywallet() {
        return "mywallet";  // /WEB-INF/views/wallet.jsp
    }
    @RequestMapping("/history")
    public String history(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/signin";
        }
        List<TradeExecution> list = tradeHistoryService.getHistory(loginUser.getId(), 30);
        model.addAttribute("history", list);
        return "history"; // /WEB-INF/views/history.jsp
    }
    
	@RequestMapping("/test")
	public String test() {
		return "test";
	}
}
