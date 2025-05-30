<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<style>
html {
	height: auto; /* 🔥 브라우저보다 긴 경우 대비 */
}

body {
	margin: 0;
	padding: 0;
	min-height: 100vh; /* 🔥 브라우저보다 작을 경우 대비 */
	background: linear-gradient(120deg, #2c3e50, #3498db);
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: flex-start;
	padding-top: 60px;
}

.signin-container {
	background-color: white;
	padding: 40px;
	border-radius: 10px;
	box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
	width: 350px;
	box-sizing: border-box;
}

.signin-container h2 {
	text-align: center;
	margin-bottom: 30px;
	color: #2c3e50;
}

.signin-container label {
	display: block;
	margin-bottom: 5px;
	font-weight: bold;
}

.signin-container input {
	width: 100%;
	padding: 10px;
	margin-bottom: 20px;
	border-radius: 5px;
	border: 1px solid #ccc;
	box-sizing: border-box;
}

.signin-container button {
	width: 100%;
	padding: 10px;
	background-color: #3498db;
	color: white;
	font-size: 16px;
	border: none;
	border-radius: 5px;
	cursor: pointer;
}

.signin-container button:hover {
	background-color: #2980b9;
}

.logo-link {
	display: block;
	width: 100%;
	text-align: center;
	margin-bottom: 20px;
}

.logo-img {
	height: 300px; /* ✅ 고정 크기 */
	margin: 0 auto; /* ✅ 수평 가운데 */
	display: block;
}
</style>
</head>
<body>
	<div class="center-wrapper">
		<a href="main" class="logo-link"> <img
			src="${pageContext.request.contextPath}/resources/img/btc-text.png"
			alt="BTC Logo" class="logo-img">
		</a>

		<div class="signin-container">
			<h2>로그인</h2>
			<form action="" method="post">
				<label for="email">이메일</label> <input type="email" id="email"
					name="email" required> <label for="password">비밀번호</label> <input
					type="password" id="password" name="password" required>

				<button type="submit">로그인</button>

				<c:if test="${error != null}">
					<p style="color: red;">${error}</p>
				</c:if>
			</form>

		</div>
</body>
</html>
