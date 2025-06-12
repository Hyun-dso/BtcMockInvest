<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BTC 거래 홈</title>

<jsp:include page="common/header.jsp" />

<script
	src="https://unpkg.com/lightweight-charts@4.1.1/dist/lightweight-charts.standalone.production.js"></script>


<!-- ✅ WebSocket 관련 -->
<script
	src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>

<script src="${pageContext.request.contextPath}/resources/js/toast.js"
	defer></script>

<!-- ✅ 기능별 스크립트 -->
<script
	src="${pageContext.request.contextPath}/resources/js/websocket.js"
	defer></script>
<!-- <script src="${pageContext.request.contextPath}/resources/js/order.js"
	defer></script> -->
<script
	src="${pageContext.request.contextPath}/resources/js/order-panel.js"
	defer></script>
<script type="module"
	src="${pageContext.request.contextPath}/resources/js/price.js" defer></script>
<script src="${pageContext.request.contextPath}/resources/js/tvchart.js"
	defer></script>
<script src="${pageContext.request.contextPath}/resources/js/btc.js"
	defer></script>
<script src="${pageContext.request.contextPath}/resources/js/volume.js"
	defer></script>
<script
	src="${pageContext.request.contextPath}/resources/js/trade-history.js"
	defer></script>
<script
	src="${pageContext.request.contextPath}/resources/js/mini-wallet.js"
	defer></script>


<!-- ✅ CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/btc.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/orderbook.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/trade-history.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/mini-wallet.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/tvchart.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/toast.css">

<!-- ✅ noUiSlider CSS -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/nouislider@15.7.1/dist/nouislider.min.css" />

<!-- ✅ noUiSlider JS -->
<script
	src="https://cdn.jsdelivr.net/npm/nouislider@15.7.1/dist/nouislider.min.js"></script>

</head>

<body data-context="${pageContext.request.contextPath}"
	data-logged-in="<%= session.getAttribute("loginUser") != null %>">
	<script>
		window.contextPath = document.body.getAttribute("data-context");
		window.loginUserId = "${sessionScope.loginUser.id}"; // ✅ 이 값이 "6"이 되도록!
	</script>
	<div class="main-container">
		<!-- 왼쪽: 커뮤니티 채팅 영역 -->
		<div class="chat-area">
			<div style="position: relative; width: 100%; height: 100%;">
				<div id="communityChatPanel"
					style="position: absolute; left: 0; top: 50px; width: 100%; height: calc(100% - 50px); background: #fff; border: 1px solid #ccc; box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1); transform: translateX(0); overflow-y: auto; padding: 15px;">
					<h3>커뮤니티채팅</h3>
					<div class="post"
						style="border: 1px solid #ddd; margin-bottom: 15px; padding: 10px;">
						<p>
							<strong>작성자:</strong> user123
						</p>
						<p>
							<strong>제목:</strong> 오늘의 BTC 전망
						</p>
						<p>BTC가 오늘 상승 흐름을 타고 있습니다. 여러분의 생각은?</p>
						<button class="like-btn">좋아요 ♥</button>
						<span>3</span>
						<p class="toggle-comments" style="color: blue; cursor: pointer;">댓글
							보기</p>
						<div class="comments"
							style="display: none; margin-top: 10px; padding-left: 10px;">
							<div class="comment-item"
								style="border-top: 1px solid #eee; padding-top: 5px;">
								<p>
									<strong>btcfan:</strong> 저도 동의합니다!
								</p>
							</div>
							<div class="comment-item"
								style="border-top: 1px solid #eee; padding-top: 5px;">
								<p>
									<strong>investKing:</strong> 조심해야 할 타이밍 같아요
								</p>
							</div>
						</div>
					</div>

					<div class="post"
						style="border: 1px solid #ddd; margin-bottom: 15px; padding: 10px;">
						<p>
							<strong>작성자:</strong> traderKim
						</p>
						<p>
							<strong>제목:</strong> 신규 진입 시점인가요?
						</p>
						<p>지금 매수 타이밍인지 고민되네요. 조언 부탁드립니다!</p>
						<button class="like-btn">좋아요 ♥</button>
						<span>1</span>
						<p class="toggle-comments" style="color: blue; cursor: pointer;">댓글
							보기</p>
						<div class="comments"
							style="display: none; margin-top: 10px; padding-left: 10px;">
							<div class="comment-item"
								style="border-top: 1px solid #eee; padding-top: 5px;">
								<p>
									<strong>userA:</strong> 아직은 기다리는게 좋아보여요
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div id="mini-wallet" class="mini-wallet">
				<div class="tabs">
					<button data-tab="balance" class="active">지갑</button>
					<button data-tab="history">체결내역</button>
					<button data-tab="orders">주문내역</button>
				</div>
				<div class="tab-content">
<div class="balance active">
	<p class="balance-item">
		<span class="label">평가 금액:</span>
		<span class="right-group">
			<span class="value" id="mini-total">0</span>
			<span class="unit">USDT</span>
		</span>
	</p>
	<p class="balance-item">
		<span class="label">수익률:</span>
		<span class="right-group">
			<span class="value" id="mini-profit">0</span>
		</span>
	</p>
	<p class="balance-item">
		<span class="label">BTC 보유 수량:</span>
		<span class="right-group">
			<span class="value" id="mini-btc">0</span>
			<span class="unit">BTC</span>
		</span>
	</p>
	<p class="balance-item">
		<span class="label">주문 가능 금액:</span>
		<span class="right-group">
			<span class="value" id="mini-usdt">0</span>
			<span class="unit">USDT</span>
		</span>
	</p>
</div>
					<div class="history">
						<ul id="mini-history"></ul>
					</div>
					<div class="orders">
						<ul id="mini-orders"></ul>
					</div>
				</div>
			</div>
		</div>

		<!-- 가운데 차트 영역 -->
		<div class="chart-area">
			<input type="checkbox" id="toggle-ma">📉 MA선 표시
			<div id="tv-chart" style="width: 100%; height: 400px;"></div>

			<!-- ⬇️ 차트 바로 아래, 버튼 작게 -->
			<div id="timeframe-selector">
				<button data-timeframe="1m">1m</button>
				<button data-timeframe="15m">15m</button>
				<button data-timeframe="1h">1h</button>
				<button data-timeframe="1d">1d</button>
				<button data-timeframe="1w">1w</button>
				<button data-timeframe="1M">1M</button>
			</div>

			<!-- 차트 아래 호가창 -->
			<!-- ✅ 바이낸스 스타일 매수/매도 UI (chart-area 아래) -->
			<div class="order-panel">
				<div class="order-box buy">
					<h4>매수 (Buy)</h4>
					<div class="mode-toggle">
						<button id="buy-limit-btn" class="limit-btn">지정가</button>
					</div>
					<label>가격 (USDT)</label> <input type="number" id="buy-price"
						readonly step="0.01" placeholder="USDT">
					<div class="bn-slider-wrapper">
						<input type="range" id="buy-slider" class="range-slider" min="0"
							max="100" value="0" />
					</div>
					<label>수량 (BTC)</label> <input type="number" id="buy-amount"
						step="0.00001" placeholder="BTC"> <label>총액 (USDT)</label>
					<input type="number" id="buy-total" step="0.01" placeholder="USDT">
					<button id="buy-submit">매수 주문 (Buy)</button>
				</div>

				<div class="order-box sell">
					<h4>매도 (Sell)</h4>
					<div class="mode-toggle">
						<button id="sell-limit-btn" class="limit-btn">지정가</button>
					</div>
					<label>가격 (USDT)</label> <input type="number" id="sell-price"
						readonly step="0.01" placeholder="USDT">
					<div class="bn-slider-wrapper">
						<input type="range" id="sell-slider" class="range-slider" min="0"
							max="100" value="0" />
					</div>
					<label>수량 (BTC)</label> <input type="number" id="sell-amount"
						step="0.00001" placeholder="BTC"> <label>총액 (USDT)</label>
					<input type="number" id="sell-total" step="0.01" placeholder="USDT">

					<button id="sell-submit">매도 주문 (Sell)</button>
				</div>
			</div>
		</div>



		<!-- 오른쪽: 거래 UI 영역 -->
		<div class="trade-area">
			<div class="trade-ui">
				<h3>호가</h3>
				<select id="tick-size">
					<option value="0.01">0.01</option>
					<option value="0.1">0.1</option>
					<option value="1">1</option>
					<option value="10">10</option>
					<option value="100">100</option>
				</select>
				<div id="mid-price"
					style="margin: 0.5rem 0; font-weight: bold; color: #FCD535;">가격:-
				</div>

				<div id="orderbook"
					style="display: flex; flex-direction: column; font-family: monospace;">
					<ul id="asks"
						style="color: #ff4d4f; list-style: none; padding: 0; margin: 0;"></ul>

					<div
						style="display: flex; align-items: center; justify-content: center; gap: 4px; margin: 1rem 0;">
						<div id="btc-price"
							style="font-size: 2rem; font-weight: bold; text-align: center;">$-</div>
					</div>

					<ul id="bids"
						style="color: #00b386; list-style: none; padding: 0; margin: 0;"></ul>
				</div>

				<div id="trade-volume" style="margin-top: 10px; font-weight: bold;">거래내역:
					-</div>

				<div id="trade-history">
					<ul id="history-list"></ul>
				</div>


			</div>
		</div>
	</div>

</body>
</html>
