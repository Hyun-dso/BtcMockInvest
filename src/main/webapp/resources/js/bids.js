export function renderBids(bids) {
  const bidsList = document.getElementById("bids");
  bidsList.innerHTML = "";
  Object.entries(bids)
    .sort((a, b) => parseFloat(a[0]) - parseFloat(b[0]))  // 오름차순
    .reverse()
    .forEach(([p, qty]) => {
      const li = document.createElement("li");
      li.textContent = `${parseFloat(p).toFixed(2)} | ${parseFloat(qty).toFixed(5)} BTC`;
      bidsList.appendChild(li);
    });
}
