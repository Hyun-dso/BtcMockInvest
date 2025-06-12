<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>부자가 되는 첫걸음</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/signup.css">
	<script src="${pageContext.request.contextPath}/resources/js/signup.js" defer></script>
</head>
<body>
	<div class="center-wrapper">
		<a href="main" class="logo-link">
			<img src="${pageContext.request.contextPath}/resources/img/btc-text.png"
				 alt="BTC Logo" class="logo-img">
		</a>

		<div class="register-container">
			<h2>회원가입</h2>
			<form action="${pageContext.request.contextPath}/signup" method="post">
				<div class="row">
					<label for="email">이메일</label>
				</div>
				<div class="row">
					<input type="email" id="email" name="email" required>
					<button type="button" class="small-btn" onclick="checkEmailDuplication()">중복확인</button>
				</div>

				<label for="password">비밀번호</label>
				<input type="password" id="password" name="password" onkeyup="checkPasswordMatch()" required>

				<label for="confirmPassword">비밀번호 확인</label>
				<input type="password" id="confirmPassword" name="confirmPassword" onkeyup="checkPasswordMatch()" required>

				<div class="pw-check-msg" id="pwMatchMsg"></div>

				<div class="row">
					<label for="username">닉네임</label>
				</div>
				<div class="row">
					<input type="text" id="username" name="username" required>
					<button type="button" class="small-btn" onclick="checkUsernameDuplication()">중복확인</button>
				</div>

				<button type="submit" class="submit-btn">가입하기</button>
			</form>
		</div>
	</div>
</body>
</html>
