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
