document.addEventListener('DOMContentLoaded', function () {
	const checkBtn = document.querySelector('.btn-check');

	if (checkBtn) {
		checkBtn.addEventListener('click', function () {
			const nickname = document.querySelector('input[name="nickname"]').value.trim();

			if (!nickname) {
				alert("닉네임을 입력해주세요.");
				return;
			}

			// 중복확인 Ajax 요청
			fetch(`/checkNickname?nickname=${encodeURIComponent(nickname)}`)
				.then(res => {
					if (!res.ok) throw new Error("서버 오류");
					return res.json();
				})
				.then(data => {
					if (data.exists) {
						alert("이미 존재하는 닉네임입니다.");
					} else {
						alert("사용 가능한 닉네임입니다.");
					}
				})
				.catch(err => {
					console.error(err);
					alert("중복확인 중 오류가 발생했습니다.");
				});
		});
	}
});
