export function renderAsks(asks) {
  const asksList = document.getElementById("asks");
  asksList.innerHTML = "";
  Object.entries(asks)
    .sort((a, b) => parseFloat(a[0]) - parseFloat(b[0]))  // 오름차순
    .reverse()
    .forEach(([p, qty]) => {
      const li = document.createElement("li");
      li.textContent = `${parseFloat(p).toFixed(2)} | ${parseFloat(qty).toFixed(5)} BTC`;
      asksList.appendChild(li);
    });
}
