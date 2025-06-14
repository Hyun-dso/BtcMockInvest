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

		<form action="updateNickname" method="post">
			<div class="form-group">
				<label>닉네임 변경</label>
				<div class="form-inline">
					<input type="text" name="nickname" class="nickname"
						placeholder="새 닉네임" required />
					<button type="button" class="btn btn-check">중복확인</button>
				</div>
			</div>

			<div class="form-group">
				<label>비밀번호 변경</label> <input type="password" name="password"
					id="password" placeholder="새 비밀번호" required /> <input
					type="password" id="passwordConfirm" placeholder="비밀번호 확인"
					onkeyup="checkPwMatch()" required />
				<div id="pwMatch"></div>
			</div>

			<button type="submit" class="btn btn-submit">변경 사항 저장</button>
		</form>

		<!-- 회원 탈퇴 버튼 -->
		<form action="withdraw" method="post"
			onsubmit="return confirm('정말 탈퇴하시겠습니까?');">
			<button type="submit" class="btn btn-delete">회원 탈퇴</button>
		</form>

		<!-- 내 지갑 보기 버튼 (한 칸 밑으로!) -->
		<div style="margin-top: 15px;">
			<a href="${pageContext.request.contextPath}/wallet"
				class="btn-wallet">← 내 지갑 보기</a>
		</div>

	</div>
</body>
</html>
