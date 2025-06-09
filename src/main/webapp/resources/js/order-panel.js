// 주문 패널 전용 스크립트
// 로그인 여부 확인
const isLoggedIn = document.body.getAttribute('data-logged-in') === 'true';

function redirectLogin() {
	window.location.href = window.contextPath + '/signin';
}

function floorToStep(value, step) {
	const factor = Math.round(1 / step);
	return Math.floor(value * factor) / factor;
}

function floorInput(el, step, decimals) {
	const v = parseFloat(el.value);
	if (!isNaN(v)) {
		el.value = floorToStep(v, step).toFixed(decimals);
	}
}

function sendOrder(type, priceId, amountId) {
	if (!isLoggedIn) {
		redirectLogin();
		return;
	}

	const priceEl = document.getElementById(priceId);
	const amountEl = document.getElementById(amountId);

	floorInput(priceEl, 0.01, 2);
	floorInput(amountEl, 0.00001, 5);

	const price = parseFloat(priceEl.value);
	const amount = parseFloat(amountEl.value);
	if (!price || !amount) {
		alert('가격과 수량을 입력하세요');
		return;
	}

	const flooredPrice = floorToStep(price, 0.01).toFixed(2);
	const flooredAmount = floorToStep(amount, 0.00001).toFixed(5);
	const total = parseFloat(flooredPrice) * parseFloat(flooredAmount);
	if (total < 1) {
		alert('총 거래 금액은 최소 1 USDT 이상이어야 합니다');
		return;
	}

	const params = new URLSearchParams();
	params.append('userId', window.loginUserId);
	params.append('type', type);
	params.append('amount', flooredAmount);
	params.append('price', flooredPrice);
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

	function setupCalc(priceId, amountId, totalId) {
		const priceEl = document.getElementById(priceId);
		const amountEl = document.getElementById(amountId);
		const totalEl = document.getElementById(totalId);
		if (!priceEl || !amountEl || !totalEl) return;

		function fromAmount() {
			const p = parseFloat(priceEl.value);
			const a = parseFloat(amountEl.value);
			if (!isNaN(p) && !isNaN(a)) {
				totalEl.value = floorToStep(p * a, 0.01).toFixed(2);
			}
		}

		function fromTotal() {
			const p = parseFloat(priceEl.value);
			const t = parseFloat(totalEl.value);
			if (!isNaN(p) && !isNaN(t) && p !== 0) {
				amountEl.value = floorToStep(t / p, 0.00001).toFixed(5);
			}
		}

		amountEl.addEventListener('input', fromAmount);
		totalEl.addEventListener('input', fromTotal);
		// 입력 칸을 다시 클릭하면 해당 필드를 비워 재입력이 가능하도록 처리
		amountEl.addEventListener('focus', () => {
		        amountEl.value = '';
		});
		totalEl.addEventListener('focus', () => {
		        totalEl.value = '';
		});
		priceEl.addEventListener('input', () => {
			fromAmount();
			fromTotal();
		});

		amountEl.addEventListener('blur', () => {
			floorInput(amountEl, 0.00001, 5);
			fromAmount();
		});
		totalEl.addEventListener('blur', () => {
			floorInput(totalEl, 0.01, 2);
			fromTotal();
		});
		priceEl.addEventListener('blur', () => {
			floorInput(priceEl, 0.01, 2);
			fromAmount();
			fromTotal();
		});

		amountEl.addEventListener('keydown', (e) => {
			if (e.key === 'Enter') {
				floorInput(amountEl, 0.00001, 5);
				fromAmount();
			}
		});
		totalEl.addEventListener('keydown', (e) => {
			if (e.key === 'Enter') {
				floorInput(totalEl, 0.01, 2);
				fromTotal();
			}
		});
		priceEl.addEventListener('keydown', (e) => {
			if (e.key === 'Enter') {
				floorInput(priceEl, 0.01, 2);
				fromAmount();
				fromTotal();
			}
		});
	}

	setupCalc('buy-price', 'buy-amount', 'buy-total');
	setupCalc('sell-price', 'sell-amount', 'sell-total');

	if (buyBtn)
		buyBtn.addEventListener('click', () => sendOrder('BUY', 'buy-price', 'buy-amount'));
	if (sellBtn)
		sellBtn.addEventListener('click', () => sendOrder('SELL', 'sell-price', 'sell-amount'));
});
