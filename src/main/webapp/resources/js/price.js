import { renderAsks } from './asks.js';
import { renderBids } from './bids.js';

let tickSize = 0.01;
let lastAsks = {};
let lastBids = {};
let lastPrice = 0;

document.addEventListener('DOMContentLoaded', () => {
  const select = document.getElementById('tick-size');
  if (select) {
    select.addEventListener('change', () => {
      tickSize = parseFloat(select.value);
	  renderAsks(lastAsks, tickSize, lastPrice);
	  renderBids(lastBids, tickSize, lastPrice);
    });
  }
});

// /topic/orderbook 구독 (호가창 업데이트)
window.websocket.connect((client) => {
  client.subscribe("/topic/orderbook", (message) => {
    try {
      const data = JSON.parse(message.body);
      const price = parseFloat(data.price);
	  lastPrice = price;
      const asks = data.asks || {};
      const bids = data.bids || {};
      const prevClose = parseFloat(data.prevClose);
      const prevCloseTime = data.prevCloseTime;
      const changeRate = ((price - prevClose) / prevClose * 100).toFixed(2);
      const color = changeRate > 0 ? "red" : changeRate < 0 ? "blue" : "gray";
      const icon = changeRate > 0 ? "▲" : changeRate < 0 ? "▼" : "-";

      // 가격 표시
      document.getElementById("btc-price").textContent = price.toLocaleString("en-US", {
        style: "currency",
        currency: "USD"
      });
      document.getElementById("btc-price").style.color = color;
      document.getElementById("btc-price").title = `기준가: $${prevClose.toFixed(2)} (${prevCloseTime})\n등락률: ${icon} ${Math.abs(changeRate)}%`;

      document.getElementById("mid-price").textContent = `가격: ${price.toFixed(2)} USDT`;

	  const bp = document.getElementById('buy-price');
	  const sp = document.getElementById('sell-price');
	  const buyLimitBtn = document.getElementById('buy-limit-btn');
	  const sellLimitBtn = document.getElementById('sell-limit-btn');
	  if (bp && !(buyLimitBtn && buyLimitBtn.classList.contains('active')))
	      bp.value = price.toFixed(2);
	  if (sp && !(sellLimitBtn && sellLimitBtn.classList.contains('active')))
	      sp.value = price.toFixed(2);

	  // 호가창 데이터 저장 및 렌더링
	  lastAsks = asks;
	  lastBids = bids;
	  lastPrice = price;
	  renderAsks(lastAsks, tickSize, lastPrice);
	  renderBids(lastBids, tickSize, lastPrice);
    } catch (e) {
      console.error("📛 시세 수신 처리 에러:", e);
    }
  });

  // /topic/price 구독 (실시간 가격 업데이트)
  client.subscribe("/topic/price", (message) => {
    try {
      const data = JSON.parse(message.body);
      const price = parseFloat(data.price);
	  lastPrice = price;

      // 실시간 가격 표시
      document.getElementById("btc-price").textContent = price.toLocaleString("en-US", {
        style: "currency",
        currency: "USD"
      });
       // 타이틀은 기준가와 등락률 정보만 표시하도록 실시간 가격 툴팁 제거
	   const bp = document.getElementById('buy-price');
	   const sp = document.getElementById('sell-price');
	   const buyLimitBtn = document.getElementById('buy-limit-btn');
	   const sellLimitBtn = document.getElementById('sell-limit-btn');
	   if (bp && !(buyLimitBtn && buyLimitBtn.classList.contains('active')))
	       bp.value = price.toFixed(2);
	   if (sp && !(sellLimitBtn && sellLimitBtn.classList.contains('active')))
	       sp.value = price.toFixed(2);
      console.log("📡 실시간 가격 수신:", price);
    } catch (e) {
      console.error("📛 실시간 가격 수신 처리 에러:", e);
    }
  });
});
