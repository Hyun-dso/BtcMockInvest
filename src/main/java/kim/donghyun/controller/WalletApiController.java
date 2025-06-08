package kim.donghyun.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kim.donghyun.model.entity.User;
import kim.donghyun.model.entity.Wallet;
import kim.donghyun.service.WalletService;
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WalletApiController {

    private final WalletService walletService;

    @GetMapping("/api/wallet")
//    public Wallet getWallet(@RequestParam Long userId) {
    public Wallet getWallet(@RequestParam("userId") Long userId) {
        return walletService.getWalletByUserId(userId);
    }
    
    @PostMapping("/api/wallet/init")
    public ResponseEntity<?> resetWallet(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
//            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        	return ResponseEntity
                    .status(401)
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .body("로그인이 필요합니다.");
        }

        Long userId = loginUser.getId();
//        System.out.println("🧠 초기화 요청 userId: " + userId);
        
        walletService.initializeWallet(userId);
//        return ResponseEntity.ok("지갑 초기화 완료");
        return ResponseEntity
                .ok()
                .header("Content-Type", "text/plain; charset=UTF-8")
                .body("지갑 초기화 완료");
    }

    @PostMapping("/api/wallet/deposit")
    public ResponseEntity<?> depositUsdt(HttpSession session, @RequestParam("amount") BigDecimal amount) {
    	User loginUser = (User) session.getAttribute("loginUser");
//        System.out.println("세션 userId = " + loginUser);  // ✅ 로그 찍기

        if (loginUser == null) {
//            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        	return ResponseEntity
                    .status(401)
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .body("로그인이 필요합니다.");
        }
        
        Long userId = loginUser.getId(); // 여기서 ID 세션 가져옴	
        
        try {
            walletService.depositUsdt(userId, amount);
//            return ResponseEntity.ok("충전 완료: $" + amount);
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .body("충전 완료: $" + amount);
        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
        	return ResponseEntity
                    .badRequest()
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .body(e.getMessage());
        }
    }   
}
