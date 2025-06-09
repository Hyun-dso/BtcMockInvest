const MAX_LEVELS = 6;

export function renderAsks(asks, tickSize = 0.01) {
  const asksList = document.getElementById("asks");
  asksList.innerHTML = "";

  const aggregated = {};
  Object.entries(asks).forEach(([p, qty]) => {
    const price = parseFloat(p);
    const key = (Math.ceil(price / tickSize) * tickSize).toFixed(2);
    aggregated[key] = (aggregated[key] || 0) + parseFloat(qty);
  });

  const entries = Object.entries(aggregated)
    .sort((a, b) => parseFloat(b[0]) - parseFloat(a[0]))
    .slice(0, MAX_LEVELS);

  for (let i = 0; i < MAX_LEVELS; i++) {
    const li = document.createElement("li");
    if (entries[i]) {
      const [p, qty] = entries[i];
	  const price = parseFloat(p);
	  li.innerHTML = `<span>${price.toFixed(2)}</span><span>${parseFloat(qty).toFixed(5)} BTC</span>`;
	  li.addEventListener("click", () => {
	    if (window.handleOrderbookClick) window.handleOrderbookClick("ASK", price);
	  });
    } else {
      li.innerHTML = `<span>-</span><span>-</span>`;
    }
    asksList.appendChild(li);
  }
}