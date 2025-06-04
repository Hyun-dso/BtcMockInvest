// ✅ 유효성 검사 함수
function isValidCandle(candle) {
  return (
    candle &&
    typeof candle.time === "number" && !isNaN(candle.time) &&
    typeof candle.open === "number" && !isNaN(candle.open) &&
    typeof candle.high === "number" && !isNaN(candle.high) &&
    typeof candle.low === "number" && !isNaN(candle.low) &&
    typeof candle.close === "number" && !isNaN(candle.close)
  );
}

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
  window.candleSeries = candleSeries;

  candleSeries.setData([]); // 초기 빈값

  // 기본 interval 설정
  window.currentInterval = "1m";
  loadCandles(window.currentInterval);

  // ✅ 실시간 WebSocket 연결
  window.websocket.connect((client) => {
    let lastCandle = null;

    client.subscribe("/topic/price", (message) => {
      if (window.currentInterval !== "1m") return;

      const price = parseFloat(message.body);
      const nowSec = Math.floor(Date.now() / 1000);

      if (isNaN(price)) {
        console.warn("❌ 수신된 가격이 NaN입니다:", message.body);
        return;
      }

      if (!lastCandle || lastCandle.time !== nowSec) {
        lastCandle = {
          time: nowSec,
          open: price,
          high: price,
          low: price,
          close: price
        };
        console.log("🕯️ 새 임시 캔들:", lastCandle);
      } else {
        lastCandle.close = price;
        lastCandle.high = Math.max(lastCandle.high, price);
        lastCandle.low = Math.min(lastCandle.low, price);
        console.log("📈 임시 캔들 갱신:", lastCandle);
      }

      const lastKnown = window.candleSeries._lastBar;
      if (!lastKnown || lastCandle.time >= lastKnown.time) {
        candleSeries.update({
          time: Number(lastCandle.time),
          open: Number(lastCandle.open),
          high: Number(lastCandle.high),
          low: Number(lastCandle.low),
          close: Number(lastCandle.close)
        });
        window.candleSeries._lastBar = { ...lastCandle }; // 마지막 시간 저장
      } else {
        console.warn("⚠️ update skipped: 과거 캔들이 들어오려 함", {
          lastChartTime: lastKnown.time,
          thisUpdateTime: lastCandle.time
        });
      }
    });

    // ✅ 정식 1분봉 수신 처리
    client.subscribe("/topic/candle/1m", (message) => {
      const candle = JSON.parse(message.body);
      if (isValidCandle(candle)) {
        const newCandle = {
          time: Number(candle.time),
          open: Number(candle.open),
          high: Number(candle.high),
          low: Number(candle.low),
          close: Number(candle.close)
        };
        candleSeries.update(newCandle);
        window.candleSeries._lastBar = newCandle; // 갱신된 정식 캔들 저장
        console.log("✅ 정식 1분봉 수신:", newCandle);
      } else {
        console.warn("❌ 정식 캔들 유효성 실패:", candle);
      }
    });
  });
});

// ✅ 초기 캔들 데이터 로드 함수
function loadCandles(timeframe) {
  const contextPath = window.contextPath || "";
  window.currentInterval = timeframe;

  fetch(`${contextPath}/api/candle?interval=${timeframe}&limit=100`)
    .then(res => res.json())
    .then(data => {
      console.log("📦 원본 응답 데이터:", data);

      const filtered = data
        .filter(isValidCandle)
        .sort((a, b) => a.time - b.time); // 시간순 정렬

      console.log("✅ 필터링 후 캔들 수:", filtered.length);
      console.table(filtered);

      if (filtered.length === 0) {
        console.warn("⚠️ 유효한 캔들 데이터가 없음");
        return;
      }

      window.candleSeries.setData(filtered);
      window.candleSeries._lastBar = filtered[filtered.length - 1]; // 마지막 캔들 기억해둠
    })
    .catch(err => {
      console.error("❌ 캔들 데이터 fetch 실패:", err);
    });
}
