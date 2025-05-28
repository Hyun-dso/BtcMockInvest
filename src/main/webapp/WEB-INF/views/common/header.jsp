<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- 헤더 스타일 정의 -->
<style>
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
	z-index: 999;
}

.logo {
	display: flex;
	align-items: center;
	gap: 10px;
	cursor: pointer;
}

.logo img {
	height: 50px;
	transition: transform 1.2s ease;
	transform-style: preserve-3d;
	backface-visibility: hidden;
}

.logo:hover .coin {
	transform: rotateY(360deg);
}

.nav-links {
	display: flex;
	gap: 25px;
}

.nav-links a {
	color: #ddd;
	text-decoration: none;
	font-size: 16px;
	position: relative;
}

.nav-links a::after {
	content: "";
	position: absolute;
	width: 0%;
	height: 2px;
	background: #f2a900;
	left: 0;
	bottom: -5px;
	transition: 0.3s;
}

.nav-links a:hover::after {
	width: 100%;
}

.nav-links a:hover {
	color: #fff;
}
</style>

<!-- 실제 헤더 HTML 구조 -->
<div class="header">
	<a href="${pageContext.request.contextPath}/home.jsp" class="logo">
		<img class="coin" src="${pageContext.request.contextPath}/resources/img/coin.png" alt="Coin" />
		<img class="text" src="${pageContext.request.contextPath}/resources/img/btc-text.png" alt="BTC" />
	</a>
	<div class="nav-links">
		<a href="${pageContext.request.contextPath}/home.jsp">홈</a>
		<a href="${pageContext.request.contextPath}/trade.jsp">거래하기</a>
		<a href="${pageContext.request.contextPath}/mypage.jsp">마이페이지</a>
		<a href="${pageContext.request.contextPath}/signin.jsp">로그인</a>
	</div>
</div>
