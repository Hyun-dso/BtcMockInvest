package kim.donghyun.repository;

import kim.donghyun.model.entity.Wallet;

public interface WalletRepository {
    void insert(Wallet wallet);
    Wallet findByUserId(Long userId);
    void updateBalance(Wallet wallet); // BTC/USDT 잔고 갱신용
}
