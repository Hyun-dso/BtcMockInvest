<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/header.css">

<!-- 실제 헤더 구조 -->
<div class="header">
	<div class="left-section">
		<a href="${pageContext.request.contextPath}/" class="logo">
			<img class="coin" src="${pageContext.request.contextPath}/resources/img/coin.png" alt="Coin" />
			<img class="text" src="${pageContext.request.contextPath}/resources/img/btc-text.png" alt="BTC" />
		</a>
		<div class="dropdown">
			<button class="dropdown-btn">거래하기 ▾</button>
			<div class="dropdown-content">
				<a href="${pageContext.request.contextPath}/btc">BTC</a>
			</div>
		</div>
	</div>
	<div class="nav-links">
		<a href="${pageContext.request.contextPath}/signin">SIGNIN</a>
		<a href="${pageContext.request.contextPath}/signup">SIGNUP</a>
	</div>
</div>
