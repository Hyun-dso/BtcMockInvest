<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>BTC 거래 홈</title>
  
  <jsp:include page="common/header.jsp" />
  
  <script src="https://unpkg.com/lightweight-charts@4.1.1/dist/lightweight-charts.standalone.production.js"></script>
  

  <!-- ✅ WebSocket 관련 -->
  <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>

  <!-- ✅ 기능별 스크립트 -->
  <script src="${pageContext.request.contextPath}/resources/js/websocket.js" defer></script>
  <script src="${pageContext.request.contextPath}/resources/js/order.js" defer></script>
  <script type="module" src="${pageContext.request.contextPath}/resources/js/price.js" defer></script>
  <script src="${pageContext.request.contextPath}/resources/js/tvchart.js" defer></script>
  <script src="${pageContext.request.contextPath}/resources/js/btc.js" defer></script>
  

  <!-- ✅ CSS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/btc.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/tvchart.css">
  
</head>

<body data-context="${pageContext.request.contextPath}" data-logged-in="<%= session.getAttribute("loginUser") != null %>">
<script>
window.contextPath = document.body.getAttribute("data-context");
window.loginUserId = "${sessionScope.loginUser.id}"; // ✅ 이 값이 "6"이 되도록!
</script>
<p>로그인 유저 ID: ${sessionScope.loginUser.id}</p>
  <div class="main-container">
    <!-- 왼쪽: 커뮤니티 채팅 영역 -->
    <div class="chat-area">
      <div style="position: relative; width: 100%; height: 100%;">
        <button id="communityChatToggle" style="position: absolute; left: 10px; top: 10px; z-index: 10;">커뮤니티 열기</button>
        <div id="communityChatPanel"
             style="position: absolute; left: 0; top: 50px; width: 100%; height: calc(100% - 50px); background: #fff; border: 1px solid #ccc; box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1); transform: translateX(-100%); transition: transform 0.3s ease; overflow-y: auto; padding: 15px;">
          <h3>커뮤니티채팅</h3>

          <div class="post" style="border: 1px solid #ddd; margin-bottom: 15px; padding: 10px;">
            <p><strong>작성자:</strong> user123</p>
            <p><strong>제목:</strong> 오늘의 BTC 전망</p>
            <p>BTC가 오늘 상승 흐름을 타고 있습니다. 여러분의 생각은?</p>
            <button class="like-btn">좋아요 ♥</button> <span>3</span>
            <p class="toggle-comments" style="color: blue; cursor: pointer;">댓글 보기</p>
            <div class="comments" style="display: none; margin-top: 10px; padding-left: 10px;">
              <div class="comment-item" style="border-top: 1px solid #eee; padding-top: 5px;">
                <p><strong>btcfan:</strong> 저도 동의합니다!</p>
              </div>
              <div class="comment-item" style="border-top: 1px solid #eee; padding-top: 5px;">
                <p><strong>investKing:</strong> 조심해야 할 타이밍 같아요</p>
              </div>
            </div>
          </div>

          <div class="post" style="border: 1px solid #ddd; margin-bottom: 15px; padding: 10px;">
            <p><strong>작성자:</strong> traderKim</p>
            <p><strong>제목:</strong> 신규 진입 시점인가요?</p>
            <p>지금 매수 타이밍인지 고민되네요. 조언 부탁드립니다!</p>
            <button class="like-btn">좋아요 ♥</button> <span>1</span>
            <p class="toggle-comments" style="color: blue; cursor: pointer;">댓글 보기</p>
            <div class="comments" style="display: none; margin-top: 10px; padding-left: 10px;">
              <div class="comment-item" style="border-top: 1px solid #eee; padding-top: 5px;">
                <p><strong>userA:</strong> 아직은 기다리는게 좋아보여요</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

<!-- 가운데 차트 영역 -->
 <div class="chart-area">
  <p>차트 영역</p>
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
    <input type="checkbox" id="toggle-ma">📉 MA선 표시
</div>

    <!-- 오른쪽: 거래 UI 영역 -->
    <div class="trade-area">
      <div class="trade-ui">
        <h3>주문 / 호가</h3>

        <div id="mid-price" style="margin: 0.5rem 0; font-weight: bold; color: #333;">가격: -</div>

        <div id="orderbook" style="display: flex; flex-direction: column; font-family: monospace;">
          <ul id="asks" style="color: red; list-style: none; padding: 0; margin: 0;"></ul>

          <div id="btc-price"
               style="font-size: 2rem; font-weight: bold; text-align: center; margin: 1rem 0;">$-</div>

          <ul id="bids" style="color: blue; list-style: none; padding: 0; margin: 0;"></ul>
        </div>

        <!-- 매수/매도 버튼 -->
        <div class="action-buttons" style="display: flex; gap: 10px; margin: 10px 0;">
        <input type="number" id="orderAmount" placeholder="수량 (BTC)" step="0.0001" />
          <button class="BuyBtn" id="buyBtn" style="color: white; background-color: rgba(255, 0, 0, 0.8); border: 2px solid transparent;">매수</button>
          <button class="sellBtn" id="sellBtn" style="color: white; background-color: rgba(0, 123, 255, 0.9); border: 2px solid transparent;">매도</button>
        </div>

        <!-- 주문 옵션 -->
        <div class="order-options" style="margin-bottom: 10px; display:flex; flex-direction:column; gap:5px;">
          <select id="orderMode">
            <option value="MARKET">Market</option>
            <option value="LIMIT">Limit</option>
          </select>
          <input type="number" id="orderPrice" placeholder="가격 (USDT)" step="0.01" style="display:none;" />
        </div>

        <!-- 수량 선택 -->
        <div class="amount-selector" style="margin-bottom: 10px;">
          <label>수량 (사용자 선택):</label>
          <select id="percentSelect">
            <option value="25">25%</option>
            <option value="50">50%</option>
            <option value="75">75%</option>
            <option value="100">100%</option>
            <option value="custom">direct input</option>
          </select>
          <input type="number" id="customInput" placeholder="ex: 0.05 BTC"
                 style="width: 100%; margin-top: 5px; display: none;" step="0.01" />
        </div>

        <!-- BTC/USDT 계산 -->
        <div class="price-display" style="margin-bottom: 10px;">
          <p>BTC: <span id="btcAmount">0.000</span></p>
          <p>USDT: <span id="usdtAmount">0.00</span></p>
        </div>

        <!-- 처리 버튼 -->
        <div class="final-buttons" style="display: flex; gap: 10px;">
          <button class="btn" id="resetBtn">Reset</button>
          <button class="btn" id="tradeBtn">Trade</button>
        </div>
      </div>
    </div>
  </div>
  
</body>
</body>
</html>
