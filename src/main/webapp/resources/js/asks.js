const MAX_LEVELS = 6;

export function renderAsks(asks) {
  const asksList = document.getElementById("asks");
  asksList.innerHTML = "";

  const entries = Object.entries(asks)
    .sort((a, b) => parseFloat(a[0]) - parseFloat(b[0])) // 오름차순
    .reverse()
    .slice(0, MAX_LEVELS);

  for (let i = 0; i < MAX_LEVELS; i++) {
    const li = document.createElement("li");
    if (entries[i]) {
      const [p, qty] = entries[i];
      li.innerHTML = `<span>${parseFloat(p).toFixed(2)}</span><span>${parseFloat(qty).toFixed(5)} BTC</span>`;
    } else {
      li.innerHTML = `<span>-</span><span>-</span>`;
    }
    asksList.appendChild(li);
  }
}