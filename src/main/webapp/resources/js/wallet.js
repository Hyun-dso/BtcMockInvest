// 💰 충전 모달 열기
function openDepositModal() {
  document.getElementById("depositModal").style.display = "block";
}

// 💰 충전 모달 닫기
function closeDepositModal() {
  document.getElementById("depositModal").style.display = "none";
}

// 💰 충전 실행
function submitDeposit() {
  const amount = document.getElementById("depositAmount").value;

  if (amount < 100 || amount > 1000000) {
    alert("100 ~ 1,000,000 USDT 사이 금액만 충전할 수 있습니다.");
    return;
  }

  fetch(contextPath + "/api/wallet/deposit", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: "amount=" + amount
  })
  .then(res => {
    if (!res.ok) {
      return res.text().then(msg => { throw new Error(msg); });
    }
    return res.text();
  })
  .then(msg => {
    alert(msg);
    closeDepositModal();
    location.reload();
  })
  .catch(err => {
    alert("❌ " + err.message);
    console.error(err);
  });
}

// ✅ 전역 함수 선언 (초기화 모달 열기/닫기용)
window.openResetModal = function () {
  const resetModal = document.getElementById("resetModal");
  const resetInput = document.getElementById("resetConfirmInput");
  const resetBtn = document.getElementById("resetConfirmBtn");

  resetModal.style.display = "block";
  resetInput.value = "";
  resetBtn.disabled = true;
  resetInput.focus();
};

window.closeResetModal = function () {
  document.getElementById("resetModal").style.display = "none";
};

// ✅ DOM 로딩 후 이벤트 바인딩
document.addEventListener("DOMContentLoaded", () => {
  // 초기화 관련
  const resetInput = document.getElementById("resetConfirmInput");
  const resetBtn = document.getElementById("resetConfirmBtn");
  const openResetModalBtn = document.getElementById("openResetModalBtn");

  if (openResetModalBtn) {
    openResetModalBtn.addEventListener("click", window.openResetModal);
  }

  if (resetInput && resetBtn) {
    resetInput.addEventListener("input", () => {
      resetBtn.disabled = resetInput.value.trim().toUpperCase() !== "RESET";
    });

    resetBtn.addEventListener("click", () => {
      if (!confirm("정말 지갑을 초기화 하시겠습니까?")) return;

      fetch(contextPath + "/api/wallet/init", {
        method: "POST",
        credentials: "include"
      })
      .then(res => {
        if (!res.ok) throw new Error("초기화 실패");
        return res.text();
      })
      .then(msg => {
        alert(msg);
        window.closeResetModal();
        location.reload();
      })
      .catch(err => {
        alert("초기화 실패: " + err.message);
      });
    });
  }

  // 💰 충전 버튼 바인딩
  const openDepositModalBtn = document.getElementById("openDepositModalBtn");
  if (openDepositModalBtn) {
    openDepositModalBtn.addEventListener("click", openDepositModal);
  }
});
