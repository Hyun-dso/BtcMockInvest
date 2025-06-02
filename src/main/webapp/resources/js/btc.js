// 슬라이드 토글
const communityBtn = document.getElementById("communityChatToggle");
const communityPanel = document.getElementById("communityChatPanel");
let isCommunityOpen = false;
communityBtn.addEventListener("click", () => {
  isCommunityOpen = !isCommunityOpen;
  communityPanel.style.transform = isCommunityOpen ? "translateX(0)" : "translateX(-100%)";
  communityBtn.textContent = isCommunityOpen ? "커뮤니티 닫기" : "커뮤니티 열기";
});

// 댓글 토글
document.querySelectorAll('.toggle-comments').forEach(toggle => {
  toggle.addEventListener('click', () => {
    const comments = toggle.nextElementSibling;
    const isOpen = comments.style.display === 'block';
    comments.style.display = isOpen ? 'none' : 'block';
    toggle.textContent = isOpen ? '댓글 보기' : '댓글 닫기';
  });
});

// WebSocket
const contextPath = document.body.getAttribute("data-context");
const socket = new SockJS(contextPath + "/ws-endpoint");
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
  console.log("✅ WebSocket 연결 성공");

  stompClient.subscribe("/topic/orderbook", (message) => {
    try {
      const data = JSON.parse(message.body);
      const price = parseFloat(data.price);
      const asks = data.asks || {};
      const bids = data.bids || {};
      const prevClose = parseFloat(data.prevClose);
      const prevCloseTime = data.prevCloseTime;
      const changeRate = ((price - prevClose) / prevClose * 100).toFixed(2);
      const color = changeRate > 0 ? "red" : changeRate < 0 ? "blue" : "gray";
      const icon = changeRate > 0 ? "▲" : changeRate < 0 ? "▼" : "-";

      const priceEl = document.getElementById("btc-price");
      priceEl.textContent = price.toLocaleString("en-US", { style: "currency", currency: "USD" });
      priceEl.style.color = color;
      priceEl.title = `기준가: $${prevClose.toFixed(2)} (${prevCloseTime})\n등락률: ${icon} ${Math.abs(changeRate)}%`;

      document.getElementById("mid-price").textContent = `가격: ${price.toFixed(2)} USDT`;

      const asksList = document.getElementById("asks");
      asksList.innerHTML = "";
      Object.entries(asks).sort((a, b) => parseFloat(a[0]) - parseFloat(b[0])).reverse().forEach(([p, qty]) => {
        const li = document.createElement("li");
        li.textContent = `${parseFloat(p).toFixed(2)} | ${parseFloat(qty).toFixed(5)} BTC`;
        asksList.appendChild(li);
      });

      const bidsList = document.getElementById("bids");
      bidsList.innerHTML = "";
      Object.entries(bids).sort((a, b) => parseFloat(a[0]) - parseFloat(b[0])).reverse().forEach(([p, qty]) => {
        const li = document.createElement("li");
        li.textContent = `${parseFloat(p).toFixed(2)} | ${parseFloat(qty).toFixed(5)} BTC`;
        bidsList.appendChild(li);
      });

    } catch (e) {
      console.error("📛 호가창 처리 중 에러:", e);
    }
  });
});

// 거래 버튼 로직
const isLoggedIn = document.body.getAttribute("data-logged-in") === "true";
const buttons = ["buyBtn", "sellBtn", "resetBtn", "tradeBtn"].map(id => document.getElementById(id));
const [buyBtn, sellBtn, resetBtn, tradeBtn] = buttons;
let tradeMode = null;

if (!isLoggedIn) {
  buttons.forEach(btn => {
    btn.disabled = true;
    btn.addEventListener('click', () => {
      alert('로그인이 필요합니다.');
      window.location.href = 'login.jsp';
    });
  });
} else {
  buyBtn.addEventListener('click', () => {
    tradeMode = '매수';
    buyBtn.style.border = '2px solid blue';
    sellBtn.style.border = '2px solid transparent';
  });

  sellBtn.addEventListener('click', () => {
    tradeMode = '매도';
    sellBtn.style.border = '2px solid red';
    buyBtn.style.border = '2px solid transparent';
  });

  tradeBtn.addEventListener('click', () => {
    if (!tradeMode) {
      alert('매수 또는 매도를 선택하세요.');
    } else {
      alert(tradeMode + '하기 실행!');
    }
  });
}

// 수량 선택 처리
const percentSelect = document.getElementById('percentSelect');
const customInput = document.getElementById('customInput');
const btcDisplay = document.getElementById('btcAmount');
const usdtDisplay = document.getElementById('usdtAmount');
let totalUsdt = 1000;

percentSelect.addEventListener('change', function () {
  const value = this.value;
  if (value === 'custom') {
    customInput.style.display = 'block';
    customInput.addEventListener('input', () => {
      const btc = parseFloat(customInput.value || 0);
      const usdt = (btc * 43180).toFixed(2);
      btcDisplay.textContent = btc.toFixed(3);
      usdtDisplay.textContent = usdt;
    });
  } else {
    customInput.style.display = 'none';
    const percent = parseInt(value);
    const useUsdt = totalUsdt * (percent / 100);
    const btc = useUsdt / 43180;
    btcDisplay.textContent = btc.toFixed(3);
    usdtDisplay.textContent = useUsdt.toFixed(2);
  }
});

// Reset
resetBtn.addEventListener('click', () => {
  btcDisplay.textContent = '0.000';
  usdtDisplay.textContent = '0.00';
  percentSelect.value = '';
  customInput.value = '';
  customInput.style.display = 'none';
});
