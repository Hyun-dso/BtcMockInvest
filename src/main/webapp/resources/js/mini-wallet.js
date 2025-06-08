document.addEventListener('DOMContentLoaded', () => {
  const ctx = window.contextPath || '';
  const userId = window.loginUserId;
  const usdtEl = document.getElementById('mini-usdt');
  const btcEl = document.getElementById('mini-btc');
  const profitEl = document.getElementById('mini-profit');
  const historyUl = document.getElementById('mini-history');

  if (userId) {
    fetch(`${ctx}/api/wallet?userId=${userId}`)
      .then(res => res.json())
      .then(w => {
        if (usdtEl) usdtEl.textContent = parseFloat(w.usdtBalance).toFixed(2);
        if (btcEl) btcEl.textContent = parseFloat(w.btcBalance).toFixed(5);
        if (profitEl) profitEl.textContent = `${w.profitRateSafe}%`;
      });

    fetch(`${ctx}/api/trade/history?userId=${userId}`)
      .then(res => res.json())
      .then(list => {
        if (!historyUl) return;
        historyUl.innerHTML = '';
        list.forEach(t => {
          const li = document.createElement('li');
          const time = t.createdAt || t.date;
          li.textContent = `${time} ${t.amount} @ ${t.price}`;
          historyUl.appendChild(li);
        });
      });
  }

  const tabs = document.querySelectorAll('#mini-wallet .tabs button');
  const sections = document.querySelectorAll('#mini-wallet .tab-content > div');
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