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

let currentSubscription = null;
let currentInterval = "1m";
let websocketClient = null;

document.addEventListener("DOMContentLoaded", () => {
  const chartContainer = document.getElementById("tv-chart");

  const chart = LightweightCharts.createChart(chartContainer, {
    width: chartContainer.clientWidth || 800,
    height: 400,
    layout: {
      backgroundColor: "#fff",
      textColor: "#000",
    },
    grid: {
      vertLines: { color: "#eee" },
      horzLines: { color: "#eee" },
    },
    timeScale: {
      timeVisible: true,
      secondsVisible: true,
    },
  });

  const candleSeries = chart.addCandlestickSeries();
  window.candleSeries = candleSeries;
  candleSeries.setData([]);

  // ✅ WebSocket 연결
  window.websocket.connect((client) => {
    console.log("🌐 WebSocket 연결 완료");
    websocketClient = client;

    // ✅ 실시간 시세 → 임시 캔들
    client.subscribe("/topic/price", (message) => {
      const { price, timestamp } = JSON.parse(message.body);
      const nowSec = Number(timestamp);

      if (!window.lastCandle || window.lastCandle.time !== nowSec) {
        window.lastCandle = {
          time: nowSec,
          open: price,
          high: price,
          low: price,
          close: price
        };
      } else {
        window.lastCandle.close = price;
        window.lastCandle.high = Math.max(window.lastCandle.high, price);
        window.lastCandle.low = Math.min(window.lastCandle.low, price);
      }

      const lastKnown = window.candleSeries._lastBar;
      console.log("⏱️ 정식봉 마지막:", lastKnown?.time, "| 실시간:", window.lastCandle.time);

      // ✅ 스마트한 update 조건: 정식 봉 시간 범위 내일 때만 update
      if (
        !lastKnown ||
        (window.lastCandle.time > lastKnown.time &&
         window.lastCandle.time <= lastKnown.time + 60)
      ) {
        candleSeries.update({ ...window.lastCandle });
      } else {
        console.warn("⚠️ 실시간 캔들이 정식 봉 범위를 벗어났습니다 → update 생략");
      }
    });

    // ✅ 정식 봉 구독
    ["1m", "15m", "1h", "1d", "1w", "1M"].forEach(interval => {
      client.subscribe(`/topic/candle/${interval}`, (message) => {
        if (interval !== currentInterval) return;

        const candle = JSON.parse(message.body);
        if (isValidCandle(candle)) {
          const newCandle = {
            time: Number(candle.time),
            open: Number(candle.open),
            high: Number(candle.high),
            low: Number(candle.low),
            close: Number(candle.close),
          };
          candleSeries.update(newCandle);
          window.candleSeries._lastBar = newCandle; // ✅ 정식 봉일 때만 lastBar 갱신
          console.log(`📩 정식 ${interval} 봉 수신:`, newCandle);
        }
      });
    });

    // ✅ 기본 interval: 1m
    subscribeToInterval("1m");
  });

  // ✅ interval 변경 시
  document.querySelectorAll('#timeframe-selector button').forEach(btn => {
    btn.addEventListener('click', () => {
      const interval = btn.dataset.timeframe;
      console.log("🖱️ 버튼 클릭됨:", interval);
      subscribeToInterval(interval);
    });
  });
});

// ✅ interval 변경 시 호출
function subscribeToInterval(interval) {
  if (!websocketClient) {
    console.warn("❌ WebSocket 아직 연결되지 않음");
    return;
  }

  if (currentSubscription) {
    currentSubscription.unsubscribe();
  }

  const contextPath = window.contextPath || "";
  currentInterval = interval;
  console.log("📡 구독 시작:", interval);

  fetch(`${contextPath}/api/candle?interval=${interval}&limit=100`)
    .then(res => res.json())
    .then(data => {
      const filtered = data.filter(isValidCandle).sort((a, b) => a.time - b.time);
      if (filtered.length === 0) {
        console.warn("⚠️ 유효한 캔들 없음");
        return;
      }
      window.candleSeries.setData(filtered);
      window.candleSeries._lastBar = filtered[filtered.length - 1]; // ✅ 초기 기준 설정
    })
    .catch(err => {
      console.error("❌ 캔들 fetch 실패:", err);
    });
}
