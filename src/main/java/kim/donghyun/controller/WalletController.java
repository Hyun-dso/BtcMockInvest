package kim.donghyun.controller;

import org.springframework.web.bind.annotation.*;

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
}
