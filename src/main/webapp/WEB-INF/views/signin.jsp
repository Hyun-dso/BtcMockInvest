<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>로그인</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/signin.css">
</head>
<body>
	<div class="center-wrapper">
		<a href="main" class="logo-link">
			<img src="${pageContext.request.contextPath}/resources/img/btc-text.png"
				 alt="BTC Logo" class="logo-img">
		</a>

		<div class="signin-container">
			<h2>로그인</h2>
			<form action="" method="post">
				<label for="email">이메일</label>
				<input type="email" id="email" name="email" required>

				<label for="password">비밀번호</label>
				<input type="password" id="password" name="password" required>

				<button type="submit">로그인</button>

				<c:if test="${error != null}">
					<p style="color: red;">${error}</p>
				</c:if>
			</form>
		</div>
	</div>
</body>
</html>
