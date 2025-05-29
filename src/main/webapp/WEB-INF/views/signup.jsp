<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
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

.register-container {
	background-color: white;
	padding: 40px;
	border-radius: 10px;
	box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
	width: 400px;
	box-sizing: border-box;
}

.register-container h2 {
	text-align: center;
	margin-bottom: 30px;
	color: #2c3e50;
}

.register-container label {
	display: block;
	margin-bottom: 5px;
	font-weight: bold;
}

.register-container input {
	width: calc(100% - 100px);
	padding: 10px;
	margin-bottom: 15px;
	border-radius: 5px;
	border: 1px solid #ccc;
	box-sizing: border-box;
}

.register-container .row {
	display: flex;
	align-items: center;
}

.register-container button.small-btn {
	margin-left: 10px;
	padding: 10px 15px;
	font-size: 14px;
	border: none;
	border-radius: 5px;
	background-color: #3498db;
	color: white;
	cursor: pointer;
}

.register-container button.small-btn:hover {
	background-color: #2980b9;
}

.register-container .submit-btn {
	width: 100%;
	padding: 12px;
	margin-top: 20px;
	background-color: #3498db;
	color: white;
	font-size: 16px;
	border: none;
	border-radius: 5px;
	cursor: pointer;
}

.register-container .submit-btn:hover {
	background-color: #2980b9;
}

.pw-check-msg {
	font-size: 12px;
	margin-top: -10px;
	margin-bottom: 10px;
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
<script>
	function checkEmailDuplication() {
		const email = document.getElementById('email').value;
		alert('이메일 중복 확인: ' + email);
	}

	function checkUsernameDuplication() {
		const username = document.getElementById('username').value;
		alert('닉네임 중복 확인: ' + username);
	}

	function checkPasswordMatch() {
		const pw = document.getElementById('password').value;
		const cpw = document.getElementById('confirmPassword').value;
		const msg = document.getElementById('pwMatchMsg');

		if (pw === cpw && pw !== '') {
			msg.textContent = '비밀번호 일치';
			msg.style.color = 'green';
		} else {
			msg.textContent = '비밀번호 불일치';
			msg.style.color = 'red';
		}
	}
</script>
</head>
<body>
	<div class="center-wrapper">
		<a href="main" class="logo-link"> <img
			src="${pageContext.request.contextPath}/resources/img/btc-text.png"
			alt="BTC Logo" class="logo-img">
		</a>

		<div class="register-container">
			<h2>회원가입</h2>
			<form action="register" method="post">
				<div class="row">
					<label for="email">이메일</label>
				</div>
				<div class="row">
					<input type="email" id="email" name="email" required>
					<button type="button" class="small-btn"
						onclick="checkEmailDuplication()">중복확인</button>
				</div>

				<label for="password">비밀번호</label> <input type="password"
					id="password" name="password" onkeyup="checkPasswordMatch()"
					required> <label for="confirmPassword">비밀번호 확인</label> <input
					type="password" id="confirmPassword" name="confirmPassword"
					onkeyup="checkPasswordMatch()" required>
				<div class="pw-check-msg" id="pwMatchMsg"></div>

				<div class="row">
					<label for="username">닉네임</label>
				</div>
				<div class="row">
					<input type="text" id="username" name="username" required>
					<button type="button" class="small-btn"
						onclick="checkUsernameDuplication()">중복확인</button>
				</div>

				<button type="submit" class="submit-btn">가입하기</button>
			</form>

		</div>
</body>
</html>
