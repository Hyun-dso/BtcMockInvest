<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="common/header.jsp" />

<!DOCTYPE html>
<html>
<head>
<title>마이페이지</title>
<style>
.mypage-container {
	max-width: 600px;
	margin: 100px auto;
	padding: 30px;
	background: #f9f9f9;
	border-radius: 10px;
	border: 1px solid #ccc;
}

h2 {
	text-align: center;
	margin-bottom: 30px;
}

.form-group {
	margin-bottom: 20px;
}

.form-inline {
	display: flex;
	gap: 10px;
	align-items: center;
}

label {
	font-weight: bold;
	margin-bottom: 5px;
	display: block;
}

input[type="text"], input[type="password"] {
	padding: 8px;
	border: 1px solid #ccc;
	border-radius: 4px;
}

input.nickname {
	width: 60%;
}

.btn {
	padding: 8px 12px;
	border: none;
	border-radius: 5px;
	cursor: pointer;
	font-weight: bold;
}

.btn-check {
	background-color: #ccc;
}

.btn-submit {
	width: 100%;
	background-color: #333;
	color: white;
	margin-top: 30px;
}

.btn-submit:hover {
	background-color: #555;
}

.btn-wallet {
	display: inline-block;
	margin-top: 10px;
	background-color: #4CAF50;
	color: white;
	text-align: center;
	padding: 8px 14px;
	text-decoration: none;
	border-radius: 5px;
	font-size: 14px;
}

.btn-delete {
	background-color: red;
	color: white;
	width: 100%;
	margin-top: 30px;
}

#pwMatch {
	color: red;
	font-size: 13px;
	margin-top: 5px;
}
</style>
<script>
	function checkPwMatch() {
		const pw1 = document.getElementById("password").value;
		const pw2 = document.getElementById("passwordConfirm").value;
		const msg = document.getElementById("pwMatch");

		if (pw1 === pw2) {
			msg.style.color = "green";
			msg.innerText = "비밀번호가 일치합니다.";
		} else {
			msg.style.color = "red";
			msg.innerText = "비밀번호가 일치하지 않습니다.";
		}
	}
</script>
</head>
<body>
	<div class="mypage-container">
		<h2>마이페이지</h2>

		<form action="updateNickname" method="post">
			<!-- 닉네임 변경 -->
			<div class="form-group">
				<label>닉네임 변경</label>
				<div class="form-inline">
					<input type="text" name="nickname" class="nickname"
						placeholder="새 닉네임" required />
					<button type="button" class="btn btn-check">중복확인</button>
				</div>
			</div>

			<!-- 비밀번호 변경 -->
			<div class="form-group">
				<label>비밀번호 변경</label> <input type="password" name="password"
					id="password" placeholder="새 비밀번호" required /> <input
					type="password" id="passwordConfirm" placeholder="비밀번호 확인"
					onkeyup="checkPwMatch()" required />
				<div id="pwMatch"></div>
			</div>

			<!-- 변경 버튼 -->
			<button type="submit" class="btn btn-submit">변경 사항 저장</button>
		</form>

		<!-- 회원 탈퇴 -->
		<form action="withdraw" method="post"
			onsubmit="return confirm('정말 탈퇴하시겠습니까?');">
			<button type="submit" class="btn btn-delete">회원 탈퇴</button>

			<!-- 내 지갑 보기 -->
			<a href="wallet" class="btn-wallet">← 내 지갑 보기</a>
		</form>
	</div>
</body>
</html>
