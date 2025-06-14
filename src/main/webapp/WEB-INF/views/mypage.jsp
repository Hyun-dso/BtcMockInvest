<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="common/header.jsp" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Page</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/mypage.css">
<script src="${pageContext.request.contextPath}/resources/js/mypage.js"
	defer></script>
</head>
<body>
	<div class="mypage-container">
		<h2>마이페이지</h2>

		<div class="mypage-wrapper">
			<div class="profile-box">
				<div class="profile-circle">풀</div>
				<div class="profile-info">
					<strong>${user.username}</strong>
					<div>${user.email}</div>
				</div>
				<div class="menu-list">
					<button class="tab-btn active" data-tab="profile">내 정보</button>
					<button class="tab-btn" data-tab="wallet">내 지갑</button>
					<button class="tab-btn" data-tab="username">닉네임 변경</button>
					<button class="tab-btn" data-tab="password">비밀번호 변경</button>
				</div>

				<div class="mypage-container">
					<div id="profile" class="tab-content active">
						<!-- 닉네임 변경 폼 -->
					</div>
					<div id="wallet" class="tab-content">
						<!-- 지갑 정보 표시 -->
					</div>
					<div id="username" class="tab-content">
						<!-- 비밀번호 변경 폼 -->
					</div>
					<div id="password" class="tab-content">
						<!-- 비밀번호 변경 폼 -->
					</div>
				</div>
			</div>

		</div>
	</div>
</body>
</html>
