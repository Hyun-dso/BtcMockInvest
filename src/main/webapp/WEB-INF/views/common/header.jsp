<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  <%-- ✅ 이거 꼭 필요 --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/header.css">

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
		<c:choose>
			<c:when test="${not empty sessionScope.loginUser}">
				<a href="${pageContext.request.contextPath}/mypage">
					${sessionScope.loginUser.username}
				</a>님 환영합니다.
				<a href="${pageContext.request.contextPath}/logout">LOGOUT</a>
			</c:when>
			<c:otherwise>
				<span>로그인이 필요합니다</span>
				<a href="${pageContext.request.contextPath}/signin">SIGNIN</a>
				<a href="${pageContext.request.contextPath}/signup">SIGNUP</a>
			</c:otherwise>
		</c:choose>
	</div>
</div>
