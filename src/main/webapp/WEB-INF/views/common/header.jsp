<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
/* 헤더 전체 영역 */
.header {
	position: fixed;
	top: 0;
	width: 100%;
	height: 80px;
	background: linear-gradient(90deg, #1a1a1a, #333);
	color: white;
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0 30px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.6);
	z-index: 1000;
	box-sizing: border-box;
	overflow: visible; /* ✅ 드롭다운이 잘리지 않도록 */
}

/* 왼쪽: 로고 + 거래하기 */
.left-section {
	display: flex;
	align-items: center;
	gap: 30px;
}

/* 로고 이미지 */
.logo {
	display: flex;
	align-items: center;
	gap: 10px;
	cursor: pointer;
}

.logo img {
	height: 80px;
	transition: transform 2s ease;
	transform-style: preserve-3d;
	backface-visibility: hidden;
}

.logo:hover .coin {
	transform: rotateY(360deg);
}

/* 거래하기 드롭다운 버튼 */
.dropdown {
	position: relative;
}

.dropdown-btn {
	color: #ddd;
	background: none;
	border: none;
	cursor: pointer;
	font-size: 16px;
}

.dropdown-content {
	display: none;
	position: absolute;
	top: 100%; /* ✅ 헤더 바로 아래로 */
	left: 0;
	background-color: #fff;
	color: black;
	box-shadow: 0 2px 6px rgba(0,0,0,0.3);
	border-radius: 5px;
	min-width: 120px;
	z-index: 1100; /* ✅ 헤더보다 위 */
}

.dropdown-content a {
	color: black;
	text-decoration: none;
	display: block;
	padding: 10px 15px;
}

.dropdown-content a:hover {
	background-color: #f1f1f1;
}

.dropdown:hover .dropdown-content {
	display: block;
}

/* 오른쪽: SIGNIN / REGIST */
.nav-links {
	display: flex;
	align-items: center;
	gap: 20px;
	white-space: nowrap;
	max-width: 100%;
	overflow: hidden;
}

.nav-links a {
	color: white !important;
	text-decoration: none;
	font-size: 14px;
	padding: 6px 12px;
	border: 1px solid #aaa;
	border-radius: 5px;
	white-space: nowrap;
}

.nav-links a:hover {
	background-color: #495057;
	color: white !important;
}
</style>

<!-- 실제 헤더 구조 -->
<div class="header">
	<div class="left-section">
		<a href="${pageContext.request.contextPath}/home.jsp" class="logo">
			<img class="coin" src="${pageContext.request.contextPath}/resources/img/coin.png" alt="Coin" />
			<img class="text" src="${pageContext.request.contextPath}/resources/img/btc-text.png" alt="BTC" />
		</a>
		<div class="dropdown">
			<button class="dropdown-btn">거래하기 ▾</button>
			<div class="dropdown-content">
				<a href="${pageContext.request.contextPath}/home.jsp">BTC</a>
			</div>
		</div>
	</div>

	<div class="nav-links">
		<a href="${pageContext.request.contextPath}/signin.jsp">SIGNIN</a>
		<a href="${pageContext.request.contextPath}/regist.jsp">REGIST</a>
	</div>
</div>
