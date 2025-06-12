<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입 | BtcMockInvest</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/theme.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/base.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/button.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/responsive.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/form-auth.css">
<script src="${pageContext.request.contextPath}/resources/js/signup.js"
	defer></script>
</head>
<body>

	<div class="signin-wrapper">
		<div class="signin-box">
			<a href="${pageContext.request.contextPath}/" class="brand-box">
				<img src="${pageContext.request.contextPath}/resources/img/logo.svg"
				alt="Logo" class="logo" />
				<h1 class="brand">BtcMockInvest</h1>
			</a>

			<h2>회원가입</h2>

			<c:if test="${not empty error}">
				<div class="error-box">${error}</div>
			</c:if>

			<form method="post"
				action="${pageContext.request.contextPath}/signup">

				<label for="email">이메일</label>
				<div class="row">
					<input type="email" id="email" name="email" required>
					<button type="button" class="small-btn"
						onclick="checkEmailDuplication()">중복확인</button>
				</div>

				<label for="password">비밀번호</label> <input type="password"
					id="password" name="password" required
					onkeyup="checkPasswordMatch()"> <label
					for="confirmPassword">비밀번호 확인</label> <input type="password"
					id="confirmPassword" name="confirmPassword" required
					onkeyup="checkPasswordMatch()">

				<div class="pw-check-msg" id="pwMatchMsg"></div>

				<label for="username">닉네임</label>
				<div class="row">
					<input type="text" id="username" name="username" required>
					<button type="button" class="small-btn"
						onclick="checkUsernameDuplication()">중복확인</button>
				</div>

				<button type="submit" class="btn-primary">가입하기</button>

				<p class="signup-msg">
					이미 계정이 있으신가요? <a href="${pageContext.request.contextPath}/signin">로그인</a>
				</p>
			</form>
		</div>
	</div>

</body>
</html>
