package kim.donghyun.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kim.donghyun.model.entity.TradeExecution;
import kim.donghyun.model.entity.User;
import kim.donghyun.model.entity.Wallet;
import kim.donghyun.service.TradeHistoryService;
import kim.donghyun.service.WalletService;
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final TradeHistoryService tradeHistoryService;
    private final WalletService walletService; // ✅ 추가
    private final PriceCache priceCache;       // ✅ 추가

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
	public String mypage(HttpSession session, Model model) {
	    User loginUser = (User) session.getAttribute("loginUser");
	    if (loginUser == null) {
	        return "redirect:/signin";
	    }

	    // 거래내역 가져오기 (기존 history.jsp와 동일)
	    List<TradeExecution> list = tradeHistoryService.getHistory(loginUser.getId(), 30);
	    model.addAttribute("tradeHistory", list);
	    
	    // 🔽 지갑 정보 추가
	    Wallet wallet = walletService.getWalletByUserId(loginUser.getId());
	    BigDecimal currentPrice = BigDecimal.valueOf(priceCache.getLatestPrice());
	    wallet.setCurrentPrice(currentPrice);

	    model.addAttribute("wallet", wallet);
	    model.addAttribute("currentPrice", currentPrice);

	    return "mypage";
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
