window.websocket.connect(client => {
  client.subscribe('/topic/trade', message => {
    try {
      const data = JSON.parse(message.body);
      const price = parseFloat(data.price).toFixed(2);
      const amount = parseFloat(data.amount).toFixed(5);
      const time = new Date(data.createdAt).toLocaleTimeString('ko-KR', { hour12: false });
      addHistoryRow(price, amount, time);
    } catch (e) {
      console.error('trade history parse error', e);
    }
  });
});

function addHistoryRow(price, amount, time) {
  const ul = document.getElementById('history-list');
  if (!ul) return;
  const li = document.createElement('li');
  li.innerHTML = `<span>${price}</span><span>${amount} BTC</span><span>${time}</span>`;
  ul.insertBefore(li, ul.firstChild);
  const max = 20;
  while (ul.children.length > max) {
    ul.removeChild(ul.lastChild);
  }
}