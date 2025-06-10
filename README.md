# BtcMockInvest
Spring Legacy 온프레미스 기반 비트코인 모의투자 서비스

DB 구조 

## 1. `user` - 사용자 정보

CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
		username VARCHAR(50) NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_login_at DATETIME DEFAULT NULL
);

---

## 2. `post` - 게시글

CREATE TABLE post (
    post_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

---

## 3. `comment` - 댓글

CREATE TABLE comment (
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES post(post_id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

## 4. `wallet` - 사용자 지갑

CREATE TABLE wallet (
    wallet_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    btc_balance DECIMAL(18,8) DEFAULT 0,
    usdt_balance DECIMAL(18,8) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

## 5. `trade_order` - 거래 내역 (매수/매도)

CREATE TABLE trade_order (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(10) NOT NULL,             -- 'BUY' or 'SELL'
    amount DECIMAL(18,8) NOT NULL,
    price DECIMAL(18,2) NOT NULL,
    total DECIMAL(18,2) NOT NULL,
    order_mode VARCHAR(10),                -- 'MARKET' or 'LIMIT'
    status VARCHAR(10) DEFAULT 'PENDING',  -- 'PENDING', 'FILLED', 'CANCELED'
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

---

## 6. `btc_price` - 실시간 시세 저장 (1초 단위)

CREATE TABLE btc_price (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    price DECIMAL(18,2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX (created_at)

);

// 추가문
ALTER TABLE btc_price
ADD UNIQUE INDEX uniq_created_at (created_at);

---

## 7~12. BTC 캔들 봉 (1분 ~ 1개월)

### 7. `btc_candle_1min`  

CREATE TABLE btc_candle_1min (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    open DECIMAL(18,2) NOT NULL,
    high DECIMAL(18,2) NOT NULL,
    low DECIMAL(18,2) NOT NULL,
    close DECIMAL(18,2) NOT NULL,
    volume DECIMAL(18,8),  -- 선택사항: 거래량
    candle_time DATETIME NOT NULL UNIQUE,
    INDEX (candle_time)
);

### 8. `btc_candle_15min`  

CREATE TABLE btc_candle_15min (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    open DECIMAL(18,2) NOT NULL,
    high DECIMAL(18,2) NOT NULL,
    low DECIMAL(18,2) NOT NULL,
    close DECIMAL(18,2) NOT NULL,
    volume DECIMAL(18,8),
    candle_time DATETIME NOT NULL UNIQUE,
    INDEX (candle_time)
);

### 9. `btc_candle_1h`  

CREATE TABLE btc_candle_1h (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    open DECIMAL(18,2) NOT NULL,
    high DECIMAL(18,2) NOT NULL,
    low DECIMAL(18,2) NOT NULL,
    close DECIMAL(18,2) NOT NULL,
    volume DECIMAL(18,8),
    candle_time DATETIME NOT NULL UNIQUE,
    INDEX (candle_time)
);

### 10. `btc_candle_1d`  

CREATE TABLE btc_candle_1d (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    open DECIMAL(18,2) NOT NULL,
    high DECIMAL(18,2) NOT NULL,
    low DECIMAL(18,2) NOT NULL,
    close DECIMAL(18,2) NOT NULL,
    volume DECIMAL(18,8),
    candle_time DATETIME NOT NULL UNIQUE,
    INDEX (candle_time)
);

### 11. `btc_candle_1w`  

CREATE TABLE btc_candle_1w (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    open DECIMAL(18,2) NOT NULL,
    high DECIMAL(18,2) NOT NULL,
    low DECIMAL(18,2) NOT NULL,
    close DECIMAL(18,2) NOT NULL,
    volume DECIMAL(18,8),
    candle_time DATETIME NOT NULL UNIQUE,
    INDEX (candle_time)
);

### 12. `btc_candle_1m`

CREATE TABLE btc_candle_1m (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    open DECIMAL(18,2) NOT NULL,
    high DECIMAL(18,2) NOT NULL,
    low DECIMAL(18,2) NOT NULL,
    close DECIMAL(18,2) NOT NULL,
    volume DECIMAL(18,8),
    candle_time DATETIME NOT NULL UNIQUE,
    INDEX (candle_time)
);

### candle 추가문

ALTER TABLE btc_candle_1min ADD UNIQUE INDEX uniq_candle_time_1min (candle_time);
ALTER TABLE btc_candle_15min ADD UNIQUE INDEX uniq_candle_time_15min (candle_time);
ALTER TABLE btc_candle_1h ADD UNIQUE INDEX uniq_candle_time_1h (candle_time);
ALTER TABLE btc_candle_1d ADD UNIQUE INDEX uniq_candle_time_1d (candle_time);
ALTER TABLE btc_candle_1w ADD UNIQUE INDEX uniq_candle_time_1w (candle_time);
ALTER TABLE btc_candle_1m ADD UNIQUE INDEX uniq_candle_time_1m (candle_time);

## 13. `order_trade` 체결 내역

CREATE TABLE order_trade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    buy_order_id BIGINT,   -- ✅ NULL 허용
    sell_order_id BIGINT,  -- ✅ NULL 허용
    
    price DECIMAL(18,2) NOT NULL,
    amount DECIMAL(18,8) NOT NULL,
    
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    -- ✅ 조건부 외래키 (둘 다 NULL 가능)
    FOREIGN KEY (buy_order_id) REFERENCES trade_order(order_id),
    FOREIGN KEY (sell_order_id) REFERENCES trade_order(order_id),
    
    INDEX (buy_order_id),
    INDEX (sell_order_id),
    INDEX (created_at)
);

# 14. wallet_reset_log(지갑 초기화 내역)

CREATE TABLE wallet_reset_log (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  reset_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  before_btc DECIMAL(18,8),
  before_usdt DECIMAL(18,8)
);

# 15 wallet_deposit_log (지갑 충전  내역)

CREATE TABLE wallet_deposit_log (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  amount DECIMAL(18,2) NOT NULL,
  before_balance DECIMAL(18,2) NOT NULL,
  after_balance DECIMAL(18,2) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

  FOREIGN KEY (user_id) REFERENCES user(id),
  INDEX (user_id),
  INDEX (created_at)
);
