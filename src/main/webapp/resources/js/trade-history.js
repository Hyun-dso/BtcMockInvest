function pad(v) {
  return String(v).padStart(2, '0');
}

function formatTime(data) {
  if (!data) return '';
  try {
    if (Array.isArray(data)) {
      const [y, m, d, h, min, s, nano] = data;
      const ms = nano ? Math.floor(nano / 1e6) : 0;
      const date = new Date(y, m - 1, d, h, min, s, ms);
      if (!isNaN(date)) {
        return `${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
      }
    } else if (typeof data === 'string') {
      let t = data;
      if (t.includes('T')) t = t.split('T')[1];
      else if (t.includes(' ')) t = t.split(' ')[1];
      return t.slice(0, 8);
    } else {
      const date = new Date(data);
      if (!isNaN(date)) {
        return `${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
      }
    }
  } catch (_) { }
  return '';
}

function loadRecentTrades() {
  const ctx = window.contextPath || '';
  fetch(ctx + '/api/trade/recent')
    .then(res => res.json())
    .then(list => {
      list.slice().reverse().forEach(item => {
        const price = parseFloat(item.price).toFixed(2);
        const amount = parseFloat(item.amount).toFixed(5);
        const type = item.type;
		const timeStr = formatTime(item.createdAt);
        addHistoryRow(price, amount, timeStr, type);
      });
    })
    .catch(e => console.error('load recent trades error', e));
}


window.websocket.connect(client => {
  client.subscribe('/topic/trade', message => {
    try {
      const data = JSON.parse(message.body);
      const price = parseFloat(data.price).toFixed(2);
      const amount = parseFloat(data.amount).toFixed(5);
	  const type = data.type;
	  const timeStr = formatTime(data.createdAt);
	  addHistoryRow(price, amount, timeStr, type);
    } catch (e) {
      console.error('trade history parse error', e);
    }
  });
});

loadRecentTrades();

function addHistoryRow(price, amount, time, type) {
  const ul = document.getElementById('history-list');
  if (!ul) return;
  const li = document.createElement('li');
  li.classList.add(type === 'BUY' ? 'buy' : 'sell');
  li.innerHTML = `<span>${price}</span><span>${amount} BTC</span><span>${time}</span>`;
  ul.insertBefore(li, ul.firstChild);
  const max = 10;
  while (ul.children.length > max) {
    ul.removeChild(ul.lastChild);
  }
}