// 주문 패널 전용 스크립트
// 로그인 여부 확인
const isLoggedIn = document.body.getAttribute('data-logged-in') === 'true';

function redirectLogin() {
  window.location.href = window.contextPath + '/signin';
}

function sendOrder(type, priceId, amountId) {
  if (!isLoggedIn) {
    redirectLogin();
    return;
  }

  const price = document.getElementById(priceId).value;
  const amount = document.getElementById(amountId).value;
  if (!price || !amount) {
    alert('가격과 수량을 입력하세요');
    return;
  }

  const params = new URLSearchParams();
  params.append('userId', window.loginUserId);
  params.append('type', type);
  params.append('amount', amount);
  params.append('price', price);
  params.append('mode', 'LIMIT');

  fetch(`${window.contextPath}/api/order/execute`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: params
  })
    .then(res => {
      if (!res.ok) throw new Error('주문 실패');
      return res.json();
    })
    .then(data => {
      alert(`✅ ${type} 주문 완료!\n체결가: ${data.price}\n수량: ${data.amount}`);
    })
    .catch(err => {
      console.error(err);
      alert('❌ 주문 중 오류 발생');
    });
}

document.addEventListener('DOMContentLoaded', () => {
  const buyBtn = document.getElementById('buy-submit');
  const sellBtn = document.getElementById('sell-submit');

  if (buyBtn)
    buyBtn.addEventListener('click', () => sendOrder('BUY', 'buy-price', 'buy-amount'));
  if (sellBtn)
    sellBtn.addEventListener('click', () => sendOrder('SELL', 'sell-price', 'sell-amount'));
});