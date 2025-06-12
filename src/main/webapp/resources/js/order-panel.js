// 주문 패널 전용 스크립트
// 로그인 여부 확인
const isLoggedIn = document.body.getAttribute('data-logged-in') === 'true';

let walletUsdt = 0;
let walletBtc = 0;
let buyCalc = {};
let sellCalc = {};

function refreshWallet(callback) {
	if (!isLoggedIn) return;
	const ctx = window.contextPath || '';
	const uid = window.loginUserId;
	if (!uid) return;
	fetch(`${ctx}/api/wallet?userId=${uid}`)
		.then(res => res.json())
		.then(w => {
			walletUsdt = parseFloat(w.usdtBalance);
			walletBtc = parseFloat(w.btcBalance);
			if (typeof callback === 'function') callback();
		});
}

if (isLoggedIn) {
	refreshWallet();
}

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

function setSliderBg(slider) {
	const val = parseFloat(slider.value || slider.dataset.value || 0);
	const thumb = slider.querySelector('.bn-slider-track-thumb');
	if (thumb) thumb.style.width = `${val}%`;
}

function initBnSlider(slider) {
	if (!slider) return;
	slider.value = parseFloat(slider.dataset.value) || 0;

	function update(e) {
		const rect = slider.getBoundingClientRect();
		const clientX = e.touches ? e.touches[0].clientX : e.clientX;
		const percent = ((clientX - rect.left) / rect.width) * 100;
		const clamped = Math.min(100, Math.max(0, percent));
		slider.value = clamped;
		slider.dataset.value = clamped;
		slider.dispatchEvent(new Event('input'));
	}

	slider.addEventListener('mousedown', e => {
		update(e);
		const move = ev => update(ev);
		const up = () => {
			document.removeEventListener('mousemove', move);
			document.removeEventListener('mouseup', up);
		};
		document.addEventListener('mousemove', move);
		document.addEventListener('mouseup', up);
	});

	slider.addEventListener('touchstart', e => {
		update(e);
		const move = ev => update(ev);
		const end = () => {
			document.removeEventListener('touchmove', move);
			document.removeEventListener('touchend', end);
		};
		document.addEventListener('touchmove', move);
		document.addEventListener('touchend', end);
	});
}

function activateLimit(btnId, priceId) {
	const btn = document.getElementById(btnId);
	const priceEl = document.getElementById(priceId);
	if (!btn || !priceEl) return;
	if (!btn.classList.contains('active')) {
		btn.classList.add('active');
		btn.checked = true;
		priceEl.removeAttribute('readonly');
	}
}

window.handleOrderbookClick = function(side, price) {
	if (side === 'ASK') {
		activateLimit('sell-limit-btn', 'sell-price');
		const sp = document.getElementById('sell-price');
		if (sp) sp.value = price.toFixed(2);
	} else if (side === 'BID') {
		activateLimit('buy-limit-btn', 'buy-price');
		const bp = document.getElementById('buy-price');
		if (bp) bp.value = price.toFixed(2);
	}
};

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
		showToast('가격과 수량을 입력하세요');
		return;
	}

	const flooredPrice = floorToStep(price, 0.01).toFixed(2);
	const flooredAmount = floorToStep(amount, 0.00001).toFixed(5);
	const total = parseFloat(flooredPrice) * parseFloat(flooredAmount);
	if (total < 1) {
		showToast('총 거래 금액은 최소 1 USDT 이상이어야 합니다');
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
			showToast(`✅ ${type} 주문 완료!\n체결가: ${data.price}\n수량: ${data.amount}`);
			refreshWallet(() => {
				if (type === 'BUY' && sellCalc.fromAmount) {
					sellCalc.fromAmount();
					sellCalc.fromTotal();
				} else if (type === 'SELL' && buyCalc.fromAmount) {
					buyCalc.fromAmount();
					buyCalc.fromTotal();
				}
			});
			if (type === 'BUY' && buyCalc.reset) buyCalc.reset();
			if (type === 'SELL' && sellCalc.reset) sellCalc.reset();
		})
		.catch(err => {
			console.error(err);
			showToast('❌ 주문 중 오류 발생');
		});
}

document.addEventListener('DOMContentLoaded', () => {
	const buyBtn = document.getElementById('buy-submit');
	const sellBtn = document.getElementById('sell-submit');
	const buyLimitBtn = document.getElementById('buy-limit-btn');
	const sellLimitBtn = document.getElementById('sell-limit-btn');

	function toggleLimit(btn, priceId) {
		const priceEl = document.getElementById(priceId);
		if (!priceEl) return;

		const active = btn.checked;
		if (active) {
			priceEl.removeAttribute('readonly');
			btn.classList.add('active');
		} else {
			priceEl.setAttribute('readonly', true);
			btn.classList.remove('active');
		}
	}

	if (buyLimitBtn)
		buyLimitBtn.addEventListener('change', () => toggleLimit(buyLimitBtn, 'buy-price'));
	if (sellLimitBtn)
		sellLimitBtn.addEventListener('change', () => toggleLimit(sellLimitBtn, 'sell-price'));

	function setupCalc(type, priceId, amountId, totalId, sliderId) {
		const priceEl = document.getElementById(priceId);
		const amountEl = document.getElementById(amountId);
		const totalEl = document.getElementById(totalId);
		const sliderEl = document.getElementById(sliderId);
		if (sliderEl) initBnSlider(sliderEl);
		if (!priceEl || !amountEl || !totalEl) return {};
		
		function fromAmount() {
			const p = parseFloat(priceEl.value);
			let a = parseFloat(amountEl.value);
			if (!isNaN(p) && !isNaN(a)) {
				let total = p * a;
				if (type === 'BUY' && total > walletUsdt) {
					total = walletUsdt;
					a = floorToStep(walletUsdt / p, 0.00001);
					amountEl.value = a.toFixed(5);
				} else if (type === 'SELL' && a > walletBtc) {
					a = walletBtc;
					amountEl.value = a.toFixed(5);
					total = p * a;
				}
				totalEl.value = floorToStep(total, 0.01).toFixed(2);
			}
			if (sliderEl) {
				const percent = type === 'BUY'
					? (parseFloat(totalEl.value) / walletUsdt) * 100
					: (parseFloat(amountEl.value) / walletBtc) * 100;
				sliderEl.value = isNaN(percent) ? 0 : Math.min(100, Math.max(0, percent));
				setSliderBg(sliderEl);
			}
		}

		function fromTotal() {
			const p = parseFloat(priceEl.value);
			let t = parseFloat(totalEl.value);
			if (!isNaN(p) && !isNaN(t) && p !== 0) {

				if (type === 'BUY' && t > walletUsdt) {
					t = walletUsdt;
					totalEl.value = t.toFixed(2);
				}
				let a = t / p;
				if (type === 'SELL' && a > walletBtc) {
					a = walletBtc;
					amountEl.value = a.toFixed(5);
					t = p * a;
					totalEl.value = floorToStep(t, 0.01).toFixed(2);
				} else {
					amountEl.value = floorToStep(a, 0.00001).toFixed(5);
				}
			}
			if (sliderEl) {
				const percent = type === 'BUY'
					? (parseFloat(totalEl.value) / walletUsdt) * 100
					: (parseFloat(amountEl.value) / walletBtc) * 100;
				sliderEl.value = isNaN(percent) ? 0 : Math.min(100, Math.max(0, percent));
				setSliderBg(sliderEl);
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
		priceEl.addEventListener('change', () => {
			floorInput(priceEl, 0.01, 2);
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

		if (sliderEl) {
			setSliderBg(sliderEl);
			sliderEl.addEventListener('input', () => {
				const percent = parseFloat(sliderEl.value);
				setSliderBg(sliderEl);
				if (type === 'BUY') {
					const usdt = walletUsdt * (percent / 100);
					totalEl.value = floorToStep(usdt, 0.01).toFixed(2);
					if (!isNaN(parseFloat(priceEl.value))) {
						const a = usdt / parseFloat(priceEl.value);
						amountEl.value = floorToStep(a, 0.00001).toFixed(5);
					}
				} else {
					const btc = walletBtc * (percent / 100);
					amountEl.value = floorToStep(btc, 0.00001).toFixed(5);
					if (!isNaN(parseFloat(priceEl.value))) {
						const t = btc * parseFloat(priceEl.value);
						totalEl.value = floorToStep(t, 0.01).toFixed(2);
					}
				}
			});
		}
		return {
			fromAmount,
			fromTotal,
			reset: () => {
				priceEl.value = '';
				amountEl.value = '';
				totalEl.value = '';
				if (sliderEl) {
					sliderEl.value = 0;
					setSliderBg(sliderEl);
				}
			}
		};
	}

	buyCalc = setupCalc('BUY', 'buy-price', 'buy-amount', 'buy-total', 'buy-slider');
	sellCalc = setupCalc('SELL', 'sell-price', 'sell-amount', 'sell-total', 'sell-slider');
	if (buyBtn)
		buyBtn.addEventListener('click', () => sendOrder('BUY', 'buy-price', 'buy-amount'));
	if (sellBtn)
		sellBtn.addEventListener('click', () => sendOrder('SELL', 'sell-price', 'sell-amount'));

	if (isLoggedIn) {
		window.websocket.connect(client => {
			client.subscribe('/topic/trade', msg => {
				const data = JSON.parse(msg.body);
				if (Number(data.userId) !== Number(window.loginUserId)) return;
				refreshWallet(() => {
					if (buyCalc.fromAmount) {
						buyCalc.fromAmount();
						buyCalc.fromTotal();
					}
					if (sellCalc.fromAmount) {
						sellCalc.fromAmount();
						sellCalc.fromTotal();
					}
				});
			});
		});
	}
});
