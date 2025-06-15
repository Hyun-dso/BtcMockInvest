document.addEventListener('DOMContentLoaded', function () {
	// 닉네임 중복확인
	const checkBtn = document.querySelector('.btn-check');
	if (checkBtn) {
		checkBtn.addEventListener('click', function () {
			const nickname = document.querySelector('input[name="nickname"]').value.trim();
			if (!nickname) {
				alert("닉네임을 입력해주세요.");
				return;
			}
			fetch(`/checkNickname?nickname=${encodeURIComponent(nickname)}`)
				.then(res => {
					if (!res.ok) throw new Error("서버 오류");
					return res.json();
				})
				.then(data => {
					alert(data.exists ? "이미 존재하는 닉네임입니다." : "사용 가능한 닉네임입니다.");
				})
				.catch(err => {
					console.error(err);
					alert("중복확인 중 오류가 발생했습니다.");
				});
		});
	}

	// 탭 전환
	const tabButtons = document.querySelectorAll(".tab-btn");
	const tabContents = document.querySelectorAll(".tab-content");

	tabButtons.forEach((btn) => {
		btn.addEventListener("click", () => {
			tabButtons.forEach((b) => b.classList.remove("active"));
			btn.classList.add("active");

			const tabId = btn.getAttribute("data-tab");
			tabContents.forEach((content) => {
				content.classList.remove("active");
				if (content.id === tabId) content.classList.add("active");
			});
		});
	});
});
document.addEventListener("DOMContentLoaded", () => {
  // 🔁 이전에 선택한 탭이 있으면 복원
  const savedTab = localStorage.getItem("selectedTab");
  if (savedTab) {
    document.querySelectorAll(".tab-btn").forEach(btn => {
      btn.classList.toggle("active", btn.dataset.tab === savedTab);
    });
    document.querySelectorAll(".tab-content").forEach(tab => {
      tab.classList.toggle("active", tab.id === savedTab);
    });
  }

  // 📌 탭 버튼 클릭 시 localStorage에 저장
  document.querySelectorAll(".tab-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      const tab = btn.dataset.tab;
      localStorage.setItem("selectedTab", tab);
    });
  });
});