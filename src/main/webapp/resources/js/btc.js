// 커뮤니티 패널
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

// 거래 버튼 및 상태 처리
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

// 수량 계산
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

// 초기화
resetBtn.addEventListener('click', () => {
  btcDisplay.textContent = '0.000';
  usdtDisplay.textContent = '0.00';
  percentSelect.value = '';
  customInput.value = '';
  customInput.style.display = 'none';
});

