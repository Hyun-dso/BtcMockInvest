// price.js

window.websocket.connect((client) => {
  client.subscribe("/topic/orderbook", (message) => {
    try {
      const data = JSON.parse(message.body);
      const price = parseFloat(data.price);
      const asks = data.asks || {};
      const bids = data.bids || {};
      const prevClose = parseFloat(data.prevClose);
      const prevCloseTime = data.prevCloseTime;
      const changeRate = ((price - prevClose) / prevClose * 100).toFixed(2);
      const color = changeRate > 0 ? "red" : changeRate < 0 ? "blue" : "gray";
      const icon = changeRate > 0 ? "▲" : changeRate < 0 ? "▼" : "-";

      const priceEl = document.getElementById("btc-price");
      priceEl.textContent = price.toLocaleString("en-US", {
        style: "currency",
        currency: "USD"
      });
      priceEl.style.color = color;
      priceEl.title = `기준가: $${prevClose.toFixed(2)} (${prevCloseTime})\n등락률: ${icon} ${Math.abs(changeRate)}%`;

      document.getElementById("mid-price").textContent = `가격: ${price.toFixed(2)} USDT`;

      const asksList = document.getElementById("asks");
      asksList.innerHTML = "";
      Object.entries(asks).sort((a, b) => parseFloat(a[0]) - parseFloat(b[0])).reverse().forEach(([p, qty]) => {
        const li = document.createElement("li");
        li.textContent = `${parseFloat(p).toFixed(2)} | ${parseFloat(qty).toFixed(5)} BTC`;
        asksList.appendChild(li);
      });

      const bidsList = document.getElementById("bids");
      bidsList.innerHTML = "";
      Object.entries(bids).sort((a, b) => parseFloat(a[0]) - parseFloat(b[0])).reverse().forEach(([p, qty]) => {
        const li = document.createElement("li");
        li.textContent = `${parseFloat(p).toFixed(2)} | ${parseFloat(qty).toFixed(5)} BTC`;
        bidsList.appendChild(li);
      });

    } catch (e) {
      console.error("📛 시세 수신 처리 에러:", e);
    }
  });
});
