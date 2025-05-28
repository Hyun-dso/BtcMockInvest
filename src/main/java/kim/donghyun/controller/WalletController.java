package kim.donghyun.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kim.donghyun.model.entity.Wallet;
import kim.donghyun.service.WalletService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/api/wallet")
    public Wallet getWallet(@RequestParam Long userId) {
        return walletService.getWalletByUserId(userId);
    }
    
    @PostMapping("/api/wallet/init")
    public ResponseEntity<?> resetWallet(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        walletService.initializeWallet(userId);
        return ResponseEntity.ok("지갑 초기화 완료");
    }

    @PostMapping("/api/wallet/deposit")
    public ResponseEntity<?> depositUsdt(HttpSession session, @RequestParam BigDecimal amount) {
        Long userId = (Long) session.getAttribute("userId");

        try {
            walletService.depositUsdt(userId, amount);
            return ResponseEntity.ok("충전 완료: $" + amount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }   
}
