<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 | BtcMockInvest</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/theme.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/base.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/button.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/responsive.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/form-auth.css">
</head>
<body>

	<div class="signin-wrapper">
		<div class="signin-box">
			<a href="${pageContext.request.contextPath}/" class="brand-box">
				<img src="${pageContext.request.contextPath}/resources/img/logo.svg"
				alt="Logo" class="logo" />
			</a>

			<h2>로그인</h2>

			<c:if test="${not empty error}">
				<div class="error-box">${error}</div>
			</c:if>

			<form method="post"
				action="${pageContext.request.contextPath}/signin">
				<label for="email">이메일</label> <input type="email" id="email"
					name="email" required> <label for="password">비밀번호</label> <input
					type="password" id="password" name="password" required>

				<button type="submit" class="btn-primary">로그인</button>
			</form>

			<p class="signup-msg">
				계정이 없으신가요? <a href="${pageContext.request.contextPath}/signup">회원가입</a>
			</p>
		</div>
	</div>

</body>
</html>
