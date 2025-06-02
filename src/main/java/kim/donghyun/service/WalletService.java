package kim.donghyun.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kim.donghyun.model.entity.Wallet;
import kim.donghyun.repository.WalletDepositLogRepository;
import kim.donghyun.repository.WalletRepository;
import kim.donghyun.repository.WalletResetLogRepository;
import kim.donghyun.util.PriceCache;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletResetLogRepository walletResetLogRepository;
    private final WalletDepositLogRepository walletDepositLogRepository;
    
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
        // 1. 유효 범위 체크
        if (amount.compareTo(BigDecimal.valueOf(100)) < 0 ||
            amount.compareTo(BigDecimal.valueOf(1_000_000)) > 0) {
            throw new IllegalArgumentException("충전 금액은 $100 이상 $1,000,000 이하로만 가능합니다.");
        }

        // 2. 기존 지갑 조회
        Wallet wallet = walletRepository.findByUserId(userId);

        // ✅ 3. 이미 BTC 또는 USDT가 있으면 충전 불가
        if (wallet.getUsdtBalance().compareTo(BigDecimal.ZERO) > 0 ||
            wallet.getBtcBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("지갑이 비어 있을 때만 충전이 가능합니다.");
        }

        // 4. 충전 진행
        BigDecimal before = wallet.getUsdtBalance();
        wallet.setUsdtBalance(before.add(amount));
        walletRepository.updateBalance(wallet);

        // 5. 충전 로그 저장
        walletDepositLogRepository.insert(userId, amount, before, wallet.getUsdtBalance());
    }
    
    public Wallet getWalletInfo(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId);

        return wallet;
    }
}
