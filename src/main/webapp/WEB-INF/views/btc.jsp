<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="common/header.jsp" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BTC 거래 홈</title>
<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

body {
	font-family: sans-serif;
	padding-top: 80px; /* ✅ 헤더 높이만큼 본문 전체를 아래로 */
	box-sizing: border-box;
}

.main-container {
	display: flex;
	height: calc(100vh - 80px); /* 헤더 빼고 꽉 차도록 */
}

.chat-area {
	width: 20%;
	border-right: 1px solid #ddd;
	padding: 10px;
}

.chart-area {
	width: 55%;
	border-right: 1px solid #ddd;
	padding: 10px;
}

.trade-area {
	width: 25%;
	padding: 10px;
}

.btn {
	padding: 6px 12px;
	border: 1px solid black;
	background-color: white;
	cursor: pointer;
}
</style>
</head>
<body>
	<!-- 본문 3분할 -->
	<div class="main-container">
		<div class="chat-area">
			<!-- 커뮤니티채팅 슬라이드 탭 영역 (왼쪽 영역 내부) -->
			<div class="chat-area"
				style="position: relative; width: 100%; height: 100%;">
				<button id="communityChatToggle"
					style="position: absolute; left: 10px; top: 10px; z-index: 10;">커뮤니티
					열기</button>
				<div id="communityChatPanel"
					style="position: absolute; left: 0; top: 50px; width: 100%; height: calc(100% - 50px); background: #fff; border: 1px solid #ccc; box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1); transform: translateX(-100%); transition: transform 0.3s ease; overflow-y: auto; padding: 15px;">
					<h3>커뮤니티채팅</h3>

					<!-- 글 목록 (임의값) -->
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

			<script>
  // 슬라이드 토글 (왼쪽 영역 내부 슬라이드)
  const communityBtn = document.getElementById("communityChatToggle");
  const communityPanel = document.getElementById("communityChatPanel");
  let isCommunityOpen = false;
  communityBtn.addEventListener("click", () => {
    isCommunityOpen = !isCommunityOpen;
    communityPanel.style.transform = isCommunityOpen ? "translateX(0)" : "translateX(-100%)";
    communityBtn.textContent = isCommunityOpen ? "커뮤니티 닫기" : "커뮤니티 열기";
  });

  // 댓글 토글
  document.querySelectorAll('.toggle-comments').forEach(toggle => {
    toggle.addEventListener('click', () => {
      const comments = toggle.nextElementSibling;
      const isOpen = comments.style.display === 'block';
      comments.style.display = isOpen ? 'none' : 'block';
      toggle.textContent = isOpen ? '댓글 보기' : '댓글 닫기';
    });
  });
</script>

		</div>
		<div class="chart-area">
			<p>차트 영역</p>
		</div>
		<div class="trade-area">
			<!-- 거래 UI 영역 (오른쪽 영역) -->
			<div class="trade-ui">
				<h3>주문 / 호가</h3>

				<!-- 🔷 실시간 BTC 시세 -->

				<!-- 현재가 -->
				<div id="mid-price"
					style="margin: 0.5rem 0; font-weight: bold; color: #333;">가격:
					-</div>
				<!-- 호가창 -->
				<div id="orderbook"
					style="display: flex; flex-direction: column; align-items: center; font-family: monospace;">
					<!-- 매도호가 -->
					<div>
						<!--     <div style="color: red; font-weight: bold;">🔺 매도호가 (ASK)</div> -->
						<ul id="asks"
							style="color: red; list-style: none; padding: 0; margin: 0;"></ul>
					</div>


					<!-- 시세 표시 -->
					<div id="btc-price"
						style="font-size: 2rem; font-weight: bold; text-align: center; margin: 1rem 0;">
						$-</div>
					<!-- 매수호가 -->
					<div>
						<!--  <div style="color: green; font-weight: bold;">▼ 매수호가 (BID)</div> -->
						<ul id="bids"
							style="color: blue; list-style: none; padding: 0; margin: 0;"></ul>
					</div>
				</div>

				<!-- 🔌 WebSocket & STOMP 연결 -->
				<script
					src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
				<script
					src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>

				<script>
  // 컨텍스트 경로 자동 추출
  const contextPath = window.location.pathname.split("/")[1]; // 예: "BtcMockInvest"
  const socket = new SockJS("/" + contextPath + "/ws-endpoint");
  const stompClient = Stomp.over(socket);

  // 연결 및 구독
  stompClient.connect({}, () => {
    console.log("✅ WebSocket 연결 성공");

    stompClient.subscribe("/topic/orderbook", (message) => {
    	  try {
    	    const data = JSON.parse(message.body);
    	    const price = parseFloat(data.price);
    	    const asks = data.asks || {};
    	    const bids = data.bids || {};

    	    // ⏬ ✅ 등락률 비교용 데이터 받기 (백엔드에서 같이 보내줘야 함!)
    	    const prevClose = parseFloat(data.prevClose);
    	    const prevCloseTime = data.prevCloseTime;

    	    // ✅ 등락률 계산 및 색상 결정
    	    const changeRate = ((price - prevClose) / prevClose * 100).toFixed(2);
    	    const color = changeRate > 0 ? "red" : changeRate < 0 ? "blue" : "gray";
    	    const icon = changeRate > 0 ? "▲" : changeRate < 0 ? "▼" : "-";

    	    // 🔸 시세 출력 + 색상 + 툴팁
    	    const priceEl = document.getElementById("btc-price");
    	    priceEl.textContent = price.toLocaleString("en-US", {
    	      style: "currency",
    	      currency: "USD"
    	    });


    	    priceEl.style.color = color;
    	    priceEl.title = `기준가: ${'$'}{parseFloat(prevClose).toLocaleString("en-US", {
    	    	  minimumFractionDigits: 2
    	    	})} (${'$'}{prevCloseTime})\n등락률: ${'$'}{icon} ${'$'}{Math.abs(changeRate)}%`;

    	    // 🔸 중앙 현재가 표시
    	    document.getElementById("mid-price").textContent = `가격: ${price.toFixed(2)} USDT`;

    	    // 🔺 매도호가
    	    const asksList = document.getElementById("asks");
    	    asksList.innerHTML = "";
    	    Object.entries(asks)
    	      .sort((a, b) => parseFloat(a[0]) - parseFloat(b[0]))
    	      .reverse()
    	      .forEach(([p, qty]) => {
    	        const li = document.createElement("li");
    	        li.textContent = `${'$'}{parseFloat(p).toLocaleString("en-US", {
    	        	  minimumFractionDigits: 2,
    	        	  maximumFractionDigits: 2
    	        	})} | ${'$'}{parseFloat(qty).toLocaleString("en-US", {
    	        	  minimumFractionDigits: 5,
    	        	  maximumFractionDigits: 5
    	        	})} BTC`;
    	        asksList.appendChild(li);
    	      });

    	    // 🔻 매수호가
    	    const bidsList = document.getElementById("bids");
    	    bidsList.innerHTML = "";
    	    Object.entries(bids)
    	      .sort((a, b) => parseFloat(a[0]) - parseFloat(b[0]))
    	      .reverse()
    	      .forEach(([p, qty]) => {
    	        const li = document.createElement("li");
    	        li.textContent = `${'$'}{parseFloat(p).toLocaleString("en-US", {
    	        	  minimumFractionDigits: 2,
    	        	  maximumFractionDigits: 2
    	        	})} | ${'$'}{parseFloat(qty).toLocaleString("en-US", {
    	        	  minimumFractionDigits: 5,
    	        	  maximumFractionDigits: 5
    	        	})} BTC`;
    	        bidsList.appendChild(li);
    	      });

    	  } catch (e) {
    	    console.error("📛 호가창 처리 중 에러:", e);
    	  }
    	});
  });
</script>

				<!-- 매수/매도 버튼 -->
				<div class="action-buttons"
					style="display: flex; gap: 10px; margin: 10px 0;">
					<button class="btn" id="buyBtn"
						style="color: white; background-color: rgba(255, 0, 0, 0.8); border: 2px solid transparent;">매수</button>
					<button class="btn" id="sellBtn"
						style="color: white; background-color: rgba(0, 123, 255, 0.9); border: 2px solid transparent;">매도</button>
				</div>

				<!-- 수량 선택 -->
				<div class="amount-selector" style="margin-bottom: 10px;">
					<label>수량 (사용자 선택):</label> <select id="percentSelect">
						<option value="25">25%</option>
						<option value="50">50%</option>
						<option value="75">75%</option>
						<option value="100">100%</option>
						<option value="custom">direct input</option>
					</select> <input type="number" id="customInput" placeholder="ex: 0.05 BTC"
						style="width: 100%; margin-top: 5px; display: none;" step="0.01" />
				</div>

				<!-- BTC/USDT 계산 표시 -->
				<div class="price-display" style="margin-bottom: 10px;">
					<p>
						BTC: <span id="btcAmount">0.000</span>
					</p>
					<p>
						USDT: <span id="usdtAmount">0.00</span>
					</p>
				</div>

				<!-- 처리 버튼 -->
				<div class="final-buttons" style="display: flex; gap: 10px;">
					<button class="btn" id="resetBtn">Reset</button>
					<button class="btn" id="tradeBtn">Trade</button>
				</div>

				<!-- 스크립트 -->
				<script>
    const isLoggedIn = <%=session.getAttribute("user") != null%>;

    const buttons = [
      document.getElementById('buyBtn'),
      document.getElementById('sellBtn'),
      document.getElementById('resetBtn'),
      document.getElementById('tradeBtn')
    ];

    const tradeBtn = document.getElementById('tradeBtn');
    let tradeMode = null;

    if (!isLoggedIn) {
      buttons.forEach(btn => {
        btn.disabled = true;
        btn.addEventListener('click', () => {
          alert('로그인이 필요합니다.');
          window.location.href = 'login.jsp';
        });
      });
    } else {
      // 로그인 상태일 때 매수/매도 선택 처리
      const buyBtn = document.getElementById('buyBtn');
      const sellBtn = document.getElementById('sellBtn');

      buyBtn.addEventListener('click', () => {
        tradeMode = '매수';
        buyBtn.style.border = '2px solid blue';
        sellBtn.style.border = '2px solid transparent';
      });

      sellBtn.addEventListener('click', () => {
        tradeMode = '매도';
        sellBtn.style.border = '2px solid red';
        buyBtn.style.border = '2px solid transparent';
      });

      tradeBtn.addEventListener('click', () => {
        if (!tradeMode) {
          alert('매수 또는 매도를 선택하세요.');
        } else {
          alert(tradeMode + '하기 실행!');
        }
      });
    }

    // 수량 선택 처리
    const percentSelect = document.getElementById('percentSelect');
    const customInput = document.getElementById('customInput');
    const btcDisplay = document.getElementById('btcAmount');
    const usdtDisplay = document.getElementById('usdtAmount');

    let totalUsdt = 1000; // 예시 자산: 1000 USDT

    percentSelect.addEventListener('change', function() {
      const value = this.value;
      if (value === 'custom') {
        customInput.style.display = 'block';
        customInput.addEventListener('input', () => {
          const btc = parseFloat(customInput.value || 0);
          const price = 43180; // 예시 BTC 가격
          const usdt = (btc * price).toFixed(2);
          btcDisplay.textContent = btc.toFixed(3);
          usdtDisplay.textContent = usdt;
        });
      } else {
        customInput.style.display = 'none';
        const percent = parseInt(value);
        const useUsdt = totalUsdt * (percent / 100);
        const btc = useUsdt / 43180;
        btcDisplay.textContent = btc.toFixed(3);
        usdtDisplay.textContent = useUsdt.toFixed(2);
      }
    });

    // Reset 버튼
    document.getElementById('resetBtn').addEventListener('click', () => {
      btcDisplay.textContent = '0.000';
      usdtDisplay.textContent = '0.00';
      percentSelect.value = '';
      customInput.value = '';
      customInput.style.display = 'none';
    });
  </script>
			</div>

		</div>
	</div>

</body>
</html>
