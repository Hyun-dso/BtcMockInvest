<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BTC 거래 홈</title>


<script
	src="https://unpkg.com/lightweight-charts@4.1.1/dist/lightweight-charts.standalone.production.js"></script>


<!-- ✅ WebSocket 관련 -->
<script
	src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>

<!-- ✅ 기능별 스크립트 -->
<script
	src="${pageContext.request.contextPath}/resources/js/websocket.js"
	defer></script>
<script src="${pageContext.request.contextPath}/resources/js/price.js"
	defer></script>
<script src="${pageContext.request.contextPath}/resources/js/tvchart.js"
	defer></script>
<script src="${pageContext.request.contextPath}/resources/js/btc.js"
	defer></script>

<!-- ✅ CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/theme.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/base.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/button.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/component.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/responsive.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/btc.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/tvchart.css">

</head>

<body data-context="${pageContext.request.contextPath}"
	data-logged-in="<%= session.getAttribute("user") != null %>">

	<jsp:include page="common/header.jsp" />

	<div class="main-container">
		<!-- 왼쪽: 커뮤니티 채팅 영역 -->
		<div class="chat-area">
			<div style="position: relative; width: 100%; height: 100%;">
				<button id="communityChatToggle"
					style="position: absolute; left: 10px; top: 10px; z-index: 10;">커뮤니티
					열기</button>
				<div id="communityChatPanel"
					style="position: absolute; left: 0; top: 50px; width: 100%; height: calc(100% - 50px); background: #fff; border: 1px solid #ccc; box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1); transform: translateX(-100%); transition: transform 0.3s ease; overflow-y: auto; padding: 15px;">
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