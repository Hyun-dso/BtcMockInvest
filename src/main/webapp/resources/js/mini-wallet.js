document.addEventListener('DOMContentLoaded', () => {
	const ctx = window.contextPath || '';
	const userId = Number(window.loginUserId);
	const usdtEl = document.getElementById('mini-usdt');
	const btcEl = document.getElementById('mini-btc');
	const totalEl = document.getElementById('mini-total');
	const profitEl = document.getElementById('mini-profit');
	const historyUl = document.getElementById('mini-history');
	const orderUl = document.getElementById('mini-orders');
	const balanceSection = document.querySelector('#mini-wallet .balance');
	const orderSection = document.querySelector('#mini-wallet .orders');
	const historySection = document.querySelector('#mini-wallet .history');

	function formatTime(timeData) {
		if (!timeData) return { display: '', tooltip: '' };
		let date;
		if (Array.isArray(timeData)) {
			const [y, mon, d, h, m, s] = timeData;
			date = new Date(y, mon - 1, d, h, m, s);
		} else if (typeof timeData === 'string') {
			date = new Date(timeData.replace(' ', 'T') + '+09:00');
		} else {
			return { display: '', tooltip: '' };
		}
		const display = date.toLocaleTimeString('ko-KR', {
			hour12: false,
			hour: '2-digit',
			minute: '2-digit',
			second: '2-digit'
		});
		const y = date.getFullYear();
		const mon = date.getMonth() + 1;
		const d = date.getDate();
		const h = date.getHours();
		const m = date.getMinutes();
		const s = date.getSeconds();
		const tooltip = `${y}년 ${String(mon).padStart(2, '0')}월 ${String(d).padStart(2, '0')}일 ` +
			`${String(h).padStart(2, '0')}시 ${String(m).padStart(2, '0')}분 ${String(s).padStart(2, '0')}초`;
		return { display, tooltip };
	}

	function setHistoryHeight() {
		const sections = [balanceSection, orderSection, historySection].filter(Boolean);
		if (sections.length > 0) {
			const refHeight = Math.max(...sections.map(s => s.offsetHeight));
			if (orderSection) orderSection.style.maxHeight = refHeight + 'px';
			if (historySection) historySection.style.maxHeight = refHeight + 'px';
		}
	}

	function refreshWallet() {
		if (!userId) return;
		fetch(`${ctx}/api/wallet?userId=${userId}`)
			.then(res => res.json())
			.then(w => {
				if (totalEl) totalEl.textContent = parseFloat(w.totalValue).toFixed(2);
				if (btcEl) btcEl.textContent = parseFloat(w.btcBalance).toFixed(8);
				if (usdtEl) usdtEl.textContent = parseFloat(w.usdtBalance).toFixed(2);
				if (profitEl) {
					const rate = parseFloat(w.profitRateValue || w.profitRateSafe);
					profitEl.textContent = `${w.profitRateSafe}%`;
					if (rate > 0) profitEl.style.color = '#F6465D';
					else if (rate < 0) profitEl.style.color = '#0ECB81';
					else profitEl.style.color = '';
				}
				setHistoryHeight();
			});
	}

	if (userId) {
		refreshWallet();

		if (orderUl) orderUl.innerHTML = '';
		if (historyUl) historyUl.innerHTML = '';

		function createOrderLi(o) {
			const li = document.createElement('li');
			li.setAttribute('data-id', o.orderId);
			let type = o.type === 'BUY' ? '매수' : '매도';
			const { display: time, tooltip } = formatTime(o.createdAt);
			const price = parseFloat(o.price).toFixed(2);
			const total = parseFloat(o.total).toFixed(2);
			const status = o.status;
			li.classList.add(o.type === 'BUY' ? 'buy' : 'sell');
			if (status === 'CANCELED') {
				type += ' 취소';
				li.classList.add('canceled');
			}
			li.innerHTML = `<span>${type}</span><span>${price}</span><span>${total}</span><span>${status}</span><span>${time}</span>`;
			if (tooltip) li.title = tooltip;
			if (status === 'PENDING') {
				const btn = document.createElement('button');
				btn.className = 'cancel-btn';
				btn.textContent = '취소';
				btn.setAttribute('data-id', o.orderId);
				btn.addEventListener('click', e => {
					const id = e.target.getAttribute('data-id');
					fetch(`${ctx}/api/order/cancel?orderId=${id}`, { method: 'POST' })
						.then(res => {
							if (!res.ok) throw new Error('fail');
							if (typeof showToast === 'function') showToast('취소 요청을 보냈습니다.');
						})
						.catch(() => {
							if (typeof showToast === 'function') showToast('취소 요청 실패');
						});
				});
				li.appendChild(btn);
			}
			return li;
		}

		fetch(`${ctx}/api/order/history?userId=${userId}&limit=20`)
		         .then(res => {
		                 if (!res.ok) throw new Error('failed');
		                 return res.json();
		         })
		         .then(list => {
		                 if (!orderUl) return;
		                 list.forEach(o => {
		                         orderUl.appendChild(createOrderLi(o));
		                 });
		                 setHistoryHeight();
		         })
		         .catch(() => { });
				 
		window.websocket.connect(client => {
			client.subscribe('/topic/pending', msg => {
				const data = JSON.parse(msg.body);
				if (!orderUl || data.userId !== userId) return;
				const existing = orderUl.querySelector(`li[data-id="${data.orderId}"]`);
				if (data.status === 'PENDING') {
				        if (existing) existing.remove();
				        orderUl.insertBefore(createOrderLi(data), orderUl.firstChild);
				} else {
				        const li = existing || createOrderLi(data);
				        li.classList.toggle('canceled', data.status === 'CANCELED');
				        const btn = li.querySelector('.cancel-btn');
				        if (btn) btn.remove();
				        const spans = li.querySelectorAll('span');
				        if (spans[0]) spans[0].textContent = data.status === 'CANCELED'
				                ? (data.type === 'BUY' ? '매수 취소' : '매도 취소')
				                : (data.type === 'BUY' ? '매수' : '매도');
				        if (spans[3]) spans[3].textContent = data.status;
				        if (!existing) {
				                orderUl.insertBefore(li, orderUl.firstChild);
				        }
				        if (data.status === 'CANCELED') {
				                if (typeof showToast === 'function') showToast('주문이 취소되었습니다.');
				        } else {
				                if (typeof showToast === 'function') showToast('주문이 체결되었습니다.');
				        }
				        refreshWallet();
				}
				const max = 20;
				while (orderUl.children.length > max) orderUl.removeChild(orderUl.lastChild);
				setHistoryHeight();
						});
			client.subscribe('/topic/trade', msg => {
				const data = JSON.parse(msg.body);
				if (data.userId !== userId) return;
				if (historyUl) {
					const li = document.createElement('li');
					const typeText = data.type === 'BUY' ? '매수' : '매도';

					let date;
					if (Array.isArray(data.createdAt)) {
						const [y, mon, d, h, m, s] = data.createdAt;
						date = new Date(y, mon - 1, d, h, m, s);
					} else if (typeof data.createdAt === 'string') {
						date = new Date(data.createdAt.replace(' ', 'T') + '+09:00');
					}
					const time = date ? date.toLocaleTimeString('ko-KR', { hour12: false, hour: '2-digit', minute: '2-digit', second: '2-digit' }) : '';

					const price = parseFloat(data.price).toFixed(2);
					const amount = parseFloat(data.amount).toFixed(5);
					li.classList.add(data.type === 'BUY' ? 'buy' : 'sell');
					li.innerHTML = `<span>${typeText}</span><span>${price}</span><span>${amount}</span><span>${time}</span>`;
					historyUl.insertBefore(li, historyUl.firstChild);
					const max = 20;
					while (historyUl.children.length > max) historyUl.removeChild(historyUl.lastChild);
				}
				refreshWallet();
				setHistoryHeight();
			});
		});

		fetch(`${ctx}/api/trade/history?userId=${userId}&limit=20`)
			.then(res => res.json())
			.then(list => {
				if (!historyUl) return;
				list.forEach(t => {
					const li = document.createElement('li');
					let timeData = t.createdAt || t.date;
					let displayTime = '';
					let tooltip = '';

					if (Array.isArray(timeData)) {
						const [y, mon, d, h, m, s] = timeData;
						const date = new Date(y, mon - 1, d, h, m, s);
						displayTime = date.toLocaleTimeString('ko-KR', {
							hour12: false,
							hour: '2-digit',
							minute: '2-digit',
							second: '2-digit'
						});
						tooltip = `${y}년 ${String(mon).padStart(2, '0')}월 ${String(d).padStart(2, '0')}일 ` +
							`${String(h).padStart(2, '0')}시 ${String(m).padStart(2, '0')}분 ${String(s).padStart(2, '0')}초`;
					} else if (typeof timeData === 'string') {
						const date = new Date(timeData.replace(' ', 'T') + '+09:00');
						displayTime = date.toLocaleTimeString('ko-KR', {
							hour12: false,
							hour: '2-digit',
							minute: '2-digit',
							second: '2-digit'
						});
						const y = date.getFullYear();
						const mon = date.getMonth() + 1;
						const d = date.getDate();
						const h = date.getHours();
						const m = date.getMinutes();
						const s = date.getSeconds();
						tooltip = `${y}년 ${String(mon).padStart(2, '0')}월 ${String(d).padStart(2, '0')}일 ` +
							`${String(h).padStart(2, '0')}시 ${String(m).padStart(2, '0')}분 ${String(s).padStart(2, '0')}초`;
					}

					const type = t.userType === 'BUY' ? '매수' : '매도';
					li.classList.add(t.userType === 'BUY' ? 'buy' : 'sell');
					const price = parseFloat(t.price).toFixed(2);
					const amount = parseFloat(t.amount).toFixed(5);
					li.innerHTML = `<span>${type}</span><span>${price}</span><span>${amount}</span><span>${displayTime}</span>`;
					if (tooltip) li.title = tooltip;
					historyUl.appendChild(li);
				});
				setHistoryHeight();
			});
	}
	const tabs = document.querySelectorAll('#mini-wallet .tabs button');
	const sections = document.querySelectorAll('#mini-wallet .tab-content > div');
	setHistoryHeight();
	window.addEventListener('resize', setHistoryHeight);
	tabs.forEach(btn => {
		btn.addEventListener('click', () => {
			tabs.forEach(b => b.classList.remove('active'));
			sections.forEach(sec => sec.classList.remove('active'));
			btn.classList.add('active');
			const target = btn.getAttribute('data-tab');
			const el = document.querySelector(`#mini-wallet .${target}`);
			if (el) {
				el.classList.add('active');
				setHistoryHeight();
			}
		});
	});
});