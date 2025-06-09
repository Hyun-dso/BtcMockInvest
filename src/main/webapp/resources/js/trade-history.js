function loadRecentTrades() {
  const ctx = window.contextPath || '';
  fetch(ctx + '/api/trade/recent')
    .then(res => res.json())
    .then(list => {
      list.slice().reverse().forEach(item => {
        const price = parseFloat(item.price).toFixed(2);
        const amount = parseFloat(item.amount).toFixed(5);
        const type = item.type;
        let timeStr = item.createdAt;
        if (typeof timeStr === 'string') {
          if (timeStr.includes('T')) {
            timeStr = timeStr.split('T')[1];
          } else if (timeStr.includes(' ')) {
            timeStr = timeStr.split(' ')[1];
          }
          timeStr = timeStr.slice(0, 8);
        }
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

	  let timeStr = data.createdAt;
	  if (typeof timeStr === 'string') {
	    if (timeStr.includes('T')) {
	      timeStr = timeStr.split('T')[1];
	    } else if (timeStr.includes(' ')) {
	      timeStr = timeStr.split(' ')[1];
	    }
	    timeStr = timeStr.slice(0, 8);
	  } else {
	    timeStr = new Date(timeStr).toLocaleTimeString('ko-KR', { hour12: false });
	  }

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