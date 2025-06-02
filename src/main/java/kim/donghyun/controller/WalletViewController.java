package kim.donghyun.controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import kim.donghyun.model.entity.User;
import kim.donghyun.model.entity.Wallet;
import kim.donghyun.service.WalletService;
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WalletViewController {

    private final WalletService walletService;
    private final PriceCache priceCache;

    @GetMapping("/wallet")
    public String walletPage(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/signin";
        }

        Wallet wallet = walletService.getWalletByUserId(loginUser.getId());

        BigDecimal currentPrice = BigDecimal.valueOf(priceCache.getLatestPrice());
        
     // currentPrice를 Wallet 객체에 세팅
        wallet.setCurrentPrice(currentPrice);
        
        model.addAttribute("wallet", wallet);
        model.addAttribute("currentPrice", currentPrice);

        return "wallet";
    }
}