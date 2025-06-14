document.addEventListener("DOMContentLoaded", function () {
  const buyBtn = document.getElementById("buyBtn");
  const sellBtn = document.getElementById("sellBtn");
  const modeSelect = document.getElementById("orderMode");
  const priceInput = document.getElementById("orderPrice");

  const userId = Number(window.loginUserId); // 실제 로그인 시 session 값 주입

  function updateOptionVisibility() {
    const mode = modeSelect.value;
    if (mode === "MARKET") {
      priceInput.style.display = "none";
    } else if (mode === "LIMIT") {
      priceInput.style.display = "block";
    } else {
      priceInput.style.display = "block";
    }
  }

  if (modeSelect) {
    modeSelect.addEventListener("change", updateOptionVisibility);
    updateOptionVisibility();
  }

  function sendOrder(type) {
    const amount = document.getElementById("orderAmount").value;
    if (!amount || isNaN(amount) || parseFloat(amount) <= 0) {
      alert("수량을 올바르게 입력해주세요");
      return;
    }

    const mode = modeSelect.value;
    const params = new URLSearchParams();
    params.append("userId", userId);
    params.append("type", type);
    params.append("amount", amount);
    params.append("mode", mode);

    const priceVal = priceInput.value;
    if (priceVal) params.append("price", priceVal);

    fetch(`${window.contextPath}/api/order/execute`, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: params,
    })
      .then((res) => {
        if (!res.ok) throw new Error("주문 실패");
        return res.json();
      })
      .then((data) => {
        alert(`✅ ${type} 주문 완료!\n체결가: ${data.price}\n수량: ${data.amount}`);
      })
      .catch((err) => {
        console.error(err);
        alert("❌ 주문 중 오류 발생");
      });
  }

  if (buyBtn) buyBtn.addEventListener("click", () => sendOrder("BUY"));
  if (sellBtn) sellBtn.addEventListener("click", () => sendOrder("SELL"));
});
