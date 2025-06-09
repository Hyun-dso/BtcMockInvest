document.addEventListener('DOMContentLoaded', () => {
	const ctx = window.contextPath || '';
	const userId = window.loginUserId;
	const usdtEl = document.getElementById('mini-usdt');
	const btcEl = document.getElementById('mini-btc');
	const totalEl = document.getElementById('mini-total');
	const profitEl = document.getElementById('mini-profit');
	const historyUl = document.getElementById('mini-history');
	const balanceSection = document.querySelector('#mini-wallet .balance');
	const historySection = document.querySelector('#mini-wallet .history');

	function setHistoryHeight() {
	        if (balanceSection && historySection) {
	                historySection.style.maxHeight = balanceSection.offsetHeight + 'px';
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
							                                if (rate > 0) profitEl.style.color = 'red';
							                                else if (rate < 0) profitEl.style.color = 'blue';
							                                else profitEl.style.color = '';
							                        }
							                        setHistoryHeight();
							                });
							}

	if (userId) {
	        refreshWallet();
	        setInterval(refreshWallet, 5000);

	        if (historyUl) historyUl.innerHTML = '';

	        fetch(`${ctx}/api/order/pending?userId=${userId}`)
	                .then(res => res.json())
	                .then(list => {
	                        if (!historyUl) return;
							list.forEach(o => {
							        const li = document.createElement('li');
	                                const type = o.type === 'BUY' ? '매수' : '매도';
	                                const d = new Date(o.createdAt.replace(' ', 'T') + '+09:00');
	                                const time = d.toLocaleTimeString('ko-KR', { hour12: false, hour: '2-digit', minute: '2-digit', second: '2-digit' });
	                                const price = parseFloat(o.price).toFixed(2);
	                                const amount = parseFloat(o.amount).toFixed(5);
	                                li.classList.add(o.type === 'BUY' ? 'buy' : 'sell');
	                                li.innerHTML = `<span>${type}</span><button class="cancel-btn" data-id="${o.orderId}">취소</button><span>${price}</span><span>${amount}</span><span>${time}</span>`;
	                                li.querySelector('.cancel-btn').addEventListener('click', e => {
	                                        const id = e.target.getAttribute('data-id');
	                                        fetch(`${ctx}/api/order/cancel?orderId=${id}`, { method: 'POST' })
	                                                .then(r => { if (r.ok) li.remove(); });
	                                });
									                historyUl.appendChild(li);
									        });
									        setHistoryHeight();
									});

					fetch(`${ctx}/api/trade/history?userId=${userId}`)
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
	                if (el) el.classList.add('active');
	        });
	});
});