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
