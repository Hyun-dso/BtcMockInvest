<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>코인을 좀 사볼까</title>

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
<script src="${pageContext.request.contextPath}/resources/js/post.js"
	defer></script>
<script
	src="${pageContext.request.contextPath}/resources/js/step-control.js"></script>

<!-- ✅ CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/btc.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/order-panel.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/community-chat.css">
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
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/postdetail.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/input-style.css">

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
		window.loginUserId = "${sessionScope.loginUser.id}";
	</script>
	<div class="main-container">
		<div class="left-container" style="height: auto">
			<!-- 왼쪽: 커뮤니티 채팅 영역 -->
			<div class="chat-area">
				<div class="chat-name" style="padding: 7px">
					<h3>커뮤니티 채팅</h3>
				</div>
				<div id="communityChatPanel" class="community-chat">

					<div id="postList"></div>

					<div id="postForm"></div>
				</div>
				<div class="chat-input-wrapper">
					<textarea id="postContent" placeholder="내용을 입력하세요"></textarea>
					<button id="postSubmit">>></button>
				</div>
			</div>
			<div class="divider-line"></div>
			<div class="divider-line"></div>
			<div id="mini-wallet" class="mini-wallet">
				<div class="tabs">
					<button data-tab="balance" class="active">지갑</button>
					<button data-tab="history">체결내역</button>
					<button data-tab="orders">주문내역</button>
				</div>
				<div class="tab-content">
					<div class="balance active">
						<p class="balance-item">
							<span class="label">평가 금액:</span> <span class="right-group">
								<span class="value" id="mini-total">0</span> <span class="unit">USDT</span>
							</span>
						</p>
						<p class="balance-item">
							<span class="label">수익률:</span> <span class="right-group">
								<span class="value" id="mini-profit">0</span>
							</span>
						</p>
						<p class="balance-item">
							<span class="label">BTC 보유 수량:</span> <span class="right-group">
								<span class="value" id="mini-btc">0</span> <span class="unit">BTC</span>
							</span>
						</p>
						<p class="balance-item">
							<span class="label">주문 가능 금액:</span> <span class="right-group">
								<span class="value" id="mini-usdt">0</span> <span class="unit">USDT</span>
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
			<div class="chart-section">
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
			</div>

			<div class="order-panel">
				<div class="order-box buy">
					<div class="order-header">
						<h4>매수 (Buy)</h4>
						<div class="mode-toggle">
							<input type="checkbox" id="buy-limit-btn" class="limit-switch" />
						</div>
					</div>
					<div class="trade-inputs">
						<div class="input-row">
							<div class="input-wrapper">
								<span class="floating-label">가격</span> <input type="number"
									id="buy-price" data-step="0.01" readonly step="0.01">
								<div class="unit-group">
									<span class="unit">USDT</span>
									<div class="step">
										<button type="button" class="step-up" data-target="buy-price">▲</button>
										<button type="button" class="step-down"
											data-target="buy-price">▼</button>
									</div>
								</div>
							</div>
						</div>

						<div class="input-row">
							<div class="input-wrapper">
								<span class="floating-label">수량</span> <input type="number"
									id="buy-amount" data-step="0.1" step="0.1">
								<div class="unit-group">
									<span class="unit">BTC</span>
									<div class="step">
										<button type="button" class="step-up" data-target="buy-amount">▲</button>
										<button type="button" class="step-down"
											data-target="buy-amount">▼</button>
									</div>
								</div>
							</div>
						</div>
						<div class="bn-slider-wrapper">
							<div id="buy-slider" class="bn-slider">
								<div class="bn-slider-track">
									<div class="bn-slider-track-thumb" style="width: 0%;"></div>
									<div class="bn-slider-handle" style="left: 0%;"></div>
									<div class="bn-slider-track-step active" style="left: 0%;"
										data-percent="0">
										<div class="bn-slider-track-step-dot"></div>
									</div>
									<div class="bn-slider-track-step" style="left: 25%;"
										data-percent="25">
										<div class="bn-slider-track-step-dot"></div>
									</div>
									<div class="bn-slider-track-step" style="left: 50%;"
										data-percent="50">
										<div class="bn-slider-track-step-dot"></div>
									</div>
									<div class="bn-slider-track-step" style="left: 75%;"
										data-percent="75">
										<div class="bn-slider-track-step-dot"></div>
									</div>
									<div class="bn-slider-track-step" style="left: 100%;"
										data-percent="100">
										<div class="bn-slider-track-step-dot"></div>
									</div>
								</div>
							</div>
						</div>
						<div class="input-row">
							<div class="input-wrapper">
								<span class="floating-label">금액</span> <input type="number"
									id="buy-total" data-step="1" step="1">
								<div class="unit-group">
									<span class="unit">USDT</span>
									<div class="step">
										<button type="button" class="step-up" data-target="buy-total">▲</button>
										<button type="button" class="step-down"
											data-target="buy-total">▼</button>
									</div>
								</div>
							</div>
						</div>
					</div>
					<c:choose>
						<c:when test="${empty sessionScope.loginUser}">
							<button id="buy-submit"
								onclick="location.href='${pageContext.request.contextPath}/signin'">Sign
								In</button>
						</c:when>
						<c:otherwise>
							<button id="buy-submit">매수 주문 (Buy)</button>
						</c:otherwise>
					</c:choose>
				</div>

				<div class="order-box sell">
					<div class="order-header">
						<h4>매도 (Sell)</h4>
						<div class="mode-toggle">
							<input type="checkbox" id="sell-limit-btn" class="limit-switch" />
						</div>
					</div>
					<div class="trade-inputs">
						<div class="input-row">
							<div class="input-wrapper">
								<span class="floating-label">가격</span> <input type="number"
									id="sell-price" data-step="0.01" readonly step="0.01">
								<div class="unit-group">
									<span class="unit">USDT</span>
									<div class="step">
										<button type="button" class="step-up" data-target="sell-price">▲</button>
										<button type="button" class="step-down"
											data-target="sell-price">▼</button>
									</div>
								</div>
							</div>
						</div>

						<div class="input-row">
							<div class="input-wrapper">
								<span class="floating-label">수량</span> <input type="number"
									id="sell-amount" data-step="0.00001" step="0.00001">
								<div class="unit-group">
									<span class="unit">BTC</span>
									<div class="step">
										<button type="button" class="step-up"
											data-target="sell-amount">▲</button>
										<button type="button" class="step-down"
											data-target="sell-amount">▼</button>
									</div>
								</div>
							</div>
						</div>
						<div class="bn-slider-wrapper">
							<div id="sell-slider" class="bn-slider">
								<div class="bn-slider-track">
									<div class="bn-slider-track-thumb" style="width: 0%;"></div>
									<div class="bn-slider-handle" style="left: 0%;"></div>
									<div class="bn-slider-track-step active" style="left: 0%;"
										data-percent="0">
										<div class="bn-slider-track-step-dot"></div>
									</div>
									<div class="bn-slider-track-step" style="left: 25%;"
										data-percent="25">
										<div class="bn-slider-track-step-dot"></div>
									</div>
									<div class="bn-slider-track-step" style="left: 50%;"
										data-percent="50">
										<div class="bn-slider-track-step-dot"></div>
									</div>
									<div class="bn-slider-track-step" style="left: 75%;"
										data-percent="75">
										<div class="bn-slider-track-step-dot"></div>
									</div>
									<div class="bn-slider-track-step" style="left: 100%;"
										data-percent="100">
										<div class="bn-slider-track-step-dot"></div>
									</div>
								</div>
							</div>
						</div>
						<div class="input-row">
							<div class="input-wrapper">
								<span class="floating-label">총액</span> <input type="number"
									id="sell-total" data-step="0.01" step="0.01">
								<div class="unit-group">
									<span class="unit">USDT</span>
									<div class="step">
										<button type="button" class="step-up" data-target="sell-total">▲</button>
										<button type="button" class="step-down"
											data-target="sell-total">▼</button>
									</div>
								</div>
							</div>
						</div>
					</div>
					<c:choose>
						<c:when test="${empty sessionScope.loginUser}">
							<button id="sell-submit"
								onclick="location.href='${pageContext.request.contextPath}/signin'">Sign
								In</button>
						</c:when>
						<c:otherwise>
							<button id="sell-submit">매도 주문 (Sell)</button>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>

		<div class="right-container">
			<div class="orderbook-area">
				<div id="mid-price"
					style="margin: 0.5rem 0; font-weight: bold; color: #EAECEF;">
					<h3>호가</h3>
					<select id="tick-size">
						<option value="0.01">0.01</option>
						<option value="0.1">0.1</option>
						<option value="1">1</option>
						<option value="10">10</option>
						<option value="100">100</option>
					</select> 가격:-
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
			</div>
			<div class="history-section">
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
