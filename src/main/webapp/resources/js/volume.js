// 실시간 거래량 표시
window.websocket.connect(client => {
  client.subscribe('/topic/volume', message => {
    try {
      const data = JSON.parse(message.body);
      const volume = parseFloat(data.volume || 0);
      const el = document.getElementById('trade-volume');
      if (el) {
		el.textContent = `거래내역: ${volume.toFixed(4)} BTC`;
      }
    } catch (e) {
      console.error('volume parse error', e);
    }
  });
});