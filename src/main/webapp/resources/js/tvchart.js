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
let maVisible = false;
let maSeries = null;

// MA 갱신을 위한 헬퍼 함수
function updateMA() {
	if (!maVisible || !maSeries) return;
	const baseData = window.candleSeries._data || [];
	const data = baseData.slice();
	if (window.lastCandle && (!data.length || window.lastCandle.time >= data[data.length - 1].time)) {
		data.push(window.lastCandle);
	}
	if (data.length === 0) return;
	const maData = calculateMA(data, 20);
	maSeries.setData(maData);
}

function calculateMA(data, period = 10) {
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
	window.chart = chart;

	// ✅ 정식봉 시리즈
	// 메인 차트 봉 색상 명시적으로 지정 (상승 시 초록, 하락 시 빨강)
	const candleSeries = chart.addCandlestickSeries({
	        upColor:  '#ff4d4f',
	        downColor: '#00b386',
	        borderUpColor: '#ff4d4f',
	        borderDownColor: '#00b386',
	        wickUpColor: '#ff4d4f',
	        wickDownColor: '#00b386'
	});

	// ✅ 툴팁 DOM 생성
	const tooltip = document.createElement('div');
	tooltip.style = `
      position: absolute;
      display: none;
      padding: 8px;
      background: rgba(0, 0, 0, 0.7);
      color: #fff;
      border-radius: 4px;
      font-size: 12px;
      pointer-events: none;
      z-index: 1000;
    `;
	document.body.appendChild(tooltip);

	// ✅ 툴팁 로직: 마우스 올릴 때
	chart.subscribeCrosshairMove(param => {
		if (!param || !param.time || !param.seriesData) {
			tooltip.style.display = 'none';
			return;
		}

		const seriesData = param.seriesData.get(candleSeries);
		if (!seriesData) {
			tooltip.style.display = 'none';
			return;
		}

		const { open, high, low, close } = seriesData;
		const date = new Date(param.time * 1000).toLocaleString('ko-KR');

		tooltip.innerHTML = `
        <strong>${date}</strong><br>
        시가: ${open}<br>
        고가: ${high}<br>
        저가: ${low}<br>
        종가: ${close}
      `;

		tooltip.style.display = 'block';
		const chartRect = chartContainer.getBoundingClientRect();
		tooltip.style.left = (chartRect.left + param.point.x + 10) + 'px';
		tooltip.style.top = (chartRect.top + param.point.y + 10) + 'px';
	});

	window.candleSeries = candleSeries;

	// ✅ 실시간 임시봉 시리즈
	const realtimeSeries = chart.addCandlestickSeries({
		upColor: 'rgba(200, 0, 0, 0.4)',
		downColor: 'rgba(0, 200, 0, 0.4)',
		borderVisible: false,
		wickVisible: false,
		crossHairMarkerVisible: true
	});
	window.realtimeSeries = realtimeSeries;

	candleSeries.setData([]);

	// ✅ MA 버튼 처리
	const maBtn = document.getElementById("toggle-ma");
	if (maBtn) {
		maBtn.addEventListener("click", () => {
			maVisible = !maVisible;

			if (maVisible) {

				if (!maSeries) {
					maSeries = chart.addLineSeries({
						color: 'rgba(0, 123, 255, 0.4)',
						lineWidth: 2,
						lineStyle: LightweightCharts.LineStyle.Solid,
						crossHairMarkerVisible: false,
					});
				}

				updateMA();
				maBtn.innerText = "📉 MA선 숨기기";
			} else {
				if (maSeries) maSeries.setData([]);
				maBtn.innerText = "📉 MA선 표시";
			}
		});
	}

	// ✅ WebSocket 연결
	// ✅ WebSocket 연결
	window.websocket.connect((client) => {
		console.log("🌐 WebSocket 연결 완료");
		websocketClient = client;

		// ✅ 실시간 시세 → 임시 캔들
		client.subscribe("/topic/price", (message) => {
			const { price, timestamp } = JSON.parse(message.body);
			const nowSec = Number(timestamp);

			// interval마다 캔들 시작 시간 계산
			const intervalSecondsMap = {
				"1m": 60,
				"15m": 60 * 15,
				"1h": 60 * 60,
				"1d": 86400,
				"1w": 86400 * 7,
				"1M": 2629743, // 평균 한 달
			};
			const step = intervalSecondsMap[currentInterval] || 60;
			const candleTime = Math.floor(nowSec / step) * step;

			if (!window.lastCandle || window.lastCandle.time !== candleTime) {
				window.lastCandle = {
					time: candleTime,
					open: price,
					high: price,
					low: price,
					close: price,
				};
			} else {
				window.lastCandle.high = Math.max(window.lastCandle.high, price);
				window.lastCandle.low = Math.min(window.lastCandle.low, price);
				window.lastCandle.close = price;
			}

			const lastKnown = window.candleSeries._lastBar;

			// ✅ 인터벌별 허용 범위 설정 (초 단위)
			const intervalAllowances = {
				"1m": 120,
				"15m": 1800,
				"1h": 3600,
				"1d": 86400 * 2,
				"1w": 86400 * 10,
				"1M": 86400 * 40
			};
			const allowance = intervalAllowances[currentInterval] || 3600;

			if (
				!lastKnown ||
				(window.lastCandle.time >= lastKnown.time &&
					window.lastCandle.time <= lastKnown.time + allowance)
			) {
				realtimeSeries.update({ ...window.lastCandle });
				chart.timeScale().scrollToRealTime();
				updateMA();
			} else {
				console.warn("⚠️ 실시간 캔들이 정식 봉 범위를 벗어났습니다 → update 생략");
			}
		}); // ✅ 여기에서 닫아야 함!!

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
					chart.timeScale().scrollToRealTime();
					window.candleSeries._lastBar = newCandle;
					window.candleSeries._data = (window.candleSeries._data || []).concat([newCandle]);
					realtimeSeries.setData([]);
					updateMA();
					console.log(`📩 정식 ${interval} 봉 수신:`, newCandle);
				}
			});
		});

		// ✅ 기본 interval
		subscribeToInterval("1m");
	});

	// ✅ interval 변경 시 버튼 처리
	document.querySelectorAll('#timeframe-selector button').forEach(btn => {
		btn.addEventListener('click', () => {
			const interval = btn.dataset.timeframe;
			// console.log("🖱️ 버튼 클릭됨:", interval);
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
				console.warn("⚠️ 유효한 캔들 없음 → 빈 데이터로 초기화 진행");
				window.candleSeries.setData([]);
				chart.timeScale().scrollToRealTime();
				window.candleSeries._lastBar = null;                // ✅ 명시적으로 초기화
				window.candleSeries._data = [];                     // ✅ MA도 비우기
				if (maSeries) maSeries.setData([]);
				return;
			}
			window.candleSeries.setData(filtered);
			chart.timeScale().scrollToRealTime();
			window.candleSeries._lastBar = filtered[filtered.length - 1];

			window.candleSeries._data = filtered;  // ✅ MA 계산용 데이터 저장
			updateMA();
		})
		.catch(err => {
			console.error("❌ 캔들 fetch 실패:", err);
		});
}