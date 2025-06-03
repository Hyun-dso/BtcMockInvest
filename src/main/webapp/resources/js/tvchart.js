document.addEventListener('DOMContentLoaded', () => {
  console.log("📈 Lightweight Chart 로딩됨");
  console.log("✅ LightweightCharts 로딩됨:", window.LightweightCharts);

  const chartContainer = document.getElementById('tv-chart');

  const chart = LightweightCharts.createChart(chartContainer, {
    width: chartContainer.clientWidth || 800,
    height: 400,
    layout: {
      backgroundColor: '#fff',
      textColor: '#000',
    },
    grid: {
      vertLines: { color: '#eee' },
      horzLines: { color: '#eee' },
    },
    timeScale: {
      timeVisible: true,
      secondsVisible: true,
    }
  });

  const candleSeries = chart.addCandlestickSeries();
  window.candleSeries = candleSeries; // 외부 접근용

  // 초기값 비워두고 실시간 반영
  candleSeries.setData([]);

  // WebSocket 연결 및 /topic/price 구독
  window.websocket.connect((client) => {
    let lastCandle = null;
    let currentSecond = null;

    client.subscribe("/topic/price", (message) => {
      const price = parseFloat(message.body);
      const nowSec = Math.floor(Date.now() / 1000) - 2; // 🔥 약간 과거로 보정

      if (isNaN(price)) {
        console.warn("❌ 수신된 가격이 NaN입니다:", message.body);
        return;
      }

      console.log("📥 실시간 가격 수신:", price);

      if (currentSecond !== nowSec) {
        // 새 캔들 시작
        lastCandle = {
          time: nowSec,
          open: price,
          high: price,
          low: price,
          close: price
        };
        console.log("🕯️ 새 캔들 생성:", lastCandle);
        candleSeries.update(lastCandle);
        currentSecond = nowSec;
      } else {
        // 기존 캔들 갱신
        lastCandle.close = price;
        lastCandle.high = Math.max(lastCandle.high, price);
        lastCandle.low = Math.min(lastCandle.low, price);
        console.log("📈 기존 캔들 갱신:", lastCandle);
        candleSeries.update(lastCandle);
      }
    });
  });
});

function loadCandles(timeframe) {
  const contextPath = window.contextPath || ""; // 안전하게 contextPath 사용
  fetch(`${contextPath}/api/candle?interval=${timeframe}&limit=100`)
    .then(res => res.json())
    .then(data => {
      if (window.candleSeries) {
        window.candleSeries.setData(data);
      }
    });
}