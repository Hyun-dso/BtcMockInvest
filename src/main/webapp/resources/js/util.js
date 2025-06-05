// ✅ utils.js - 차트 공통 함수 모듈

// ✅ 캔들 유효성 검사
// 📁 util.js
window.chartUtils = {
  isValidCandle: function(candle) {
    return (
      candle &&
      typeof candle.time === "number" && !isNaN(candle.time) &&
      typeof candle.open === "number" && !isNaN(candle.open) &&
      typeof candle.high === "number" && !isNaN(candle.high) &&
      typeof candle.low === "number" && !isNaN(candle.low) &&
      typeof candle.close === "number" && !isNaN(candle.close)
    );
  },

  calculateMA: function(data, period = 10) {
    const result = [];
    const k = 2 / (period + 1);
    let ema = data[0].close;

    for (let i = 0; i < data.length; i++) {
      if (i === 0) {
        ema = data[0].close;
      } else {
        ema = data[i].close * k + ema * (1 - k);
      }
      result.push({
        time: data[i].time,
        value: ema
      });
    }

    return result;
  }
};


// ✅ interval별 기준 시간 계산 함수
export function getCandleTimeByInterval(timestamp, interval) {
  const sec = Number(timestamp);
  switch (interval) {
    case "1m": return Math.floor(sec / 60) * 60;
    case "15m": return Math.floor(sec / 900) * 900;
    case "1h": return Math.floor(sec / 3600) * 3600;
    case "1d": {
      const d = new Date(sec * 1000);
      d.setUTCHours(0, 0, 0, 0);
      return Math.floor(d.getTime() / 1000);
    }
    case "1w": {
      const d = new Date(sec * 1000);
      const day = d.getUTCDay();
      d.setUTCDate(d.getUTCDate() - day);
      d.setUTCHours(0, 0, 0, 0);
      return Math.floor(d.getTime() / 1000);
    }
    case "1M": {
      const d = new Date(sec * 1000);
      d.setUTCDate(1);
      d.setUTCHours(0, 0, 0, 0);
      return Math.floor(d.getTime() / 1000);
    }
    default: return Math.floor(sec / 60) * 60;
  }
}
