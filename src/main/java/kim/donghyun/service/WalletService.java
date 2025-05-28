package kim.donghyun.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kim.donghyun.model.entity.Wallet;
import kim.donghyun.repository.WalletRepository;
import kim.donghyun.repository.WalletResetLogRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletResetLogRepository walletResetLogRepository;

    @Transactional
    public boolean applyTrade(Long userId, BigDecimal price, BigDecimal amount, String type) {
        Wallet wallet = walletRepository.findByUserId(userId);
        BigDecimal usdt = price.multiply(amount);

        if ("BUY".equalsIgnoreCase(type)) {
            if (wallet.getUsdtBalance().compareTo(usdt) < 0) return false;
            wallet.setUsdtBalance(wallet.getUsdtBalance().subtract(usdt));
            wallet.setBtcBalance(wallet.getBtcBalance().add(amount));
        } else {
            if (wallet.getBtcBalance().compareTo(amount) < 0) return false;
            wallet.setBtcBalance(wallet.getBtcBalance().subtract(amount));
            wallet.setUsdtBalance(wallet.getUsdtBalance().add(usdt));
        }

        walletRepository.updateBalance(wallet);
        return true;
    }
    
    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId);
    }
    
    @Transactional
    public void initializeWallet(Long userId) {
        // ✅ 1. DB에서 현재 지갑 상태 불러오기
        Wallet wallet = walletRepository.findByUserId(userId);

        // ✅ 2. 로그 저장 (초기화 전 잔고 기준)
        walletResetLogRepository.insertLog(
            userId,
            wallet.getBtcBalance(),
            wallet.getUsdtBalance()
        );

        // ✅ 3. 잔고 0으로 초기화
        wallet.setBtcBalance(BigDecimal.ZERO);
        wallet.setUsdtBalance(BigDecimal.ZERO);
        walletRepository.updateBalance(wallet);
    }

    public void depositUsdt(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId);
        wallet.setUsdtBalance(wallet.getUsdtBalance().add(amount));
        walletRepository.updateBalance(wallet);
    }
}
