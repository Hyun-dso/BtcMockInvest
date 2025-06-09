const MAX_LEVELS = 6;

export function renderBids(bids, tickSize = 0.01) {
  const bidsList = document.getElementById("bids");
  bidsList.innerHTML = "";

  const aggregated = {};
  Object.entries(bids).forEach(([p, qty]) => {
    const price = parseFloat(p);
    const key = (Math.floor(price / tickSize) * tickSize).toFixed(2);
    aggregated[key] = (aggregated[key] || 0) + parseFloat(qty);
  });

  const entries = Object.entries(aggregated)
    .sort((a, b) => parseFloat(a[0]) - parseFloat(b[0]))
	//     .sort((a, b) => parseFloat(a[0]) - parseFloat(b[0])) // 오름차순
    .slice(0, MAX_LEVELS);

  for (let i = 0; i < MAX_LEVELS; i++) {
    const li = document.createElement("li");
    if (entries[i]) {
      const [p, qty] = entries[i];
	  const price = parseFloat(p);
	  li.innerHTML = `<span>${price.toFixed(2)}</span><span>${parseFloat(qty).toFixed(5)} BTC</span>`;
	  li.addEventListener("click", () => {
	    if (window.handleOrderbookClick) window.handleOrderbookClick("BID", price);
	  });
    } else {
      li.innerHTML = `<span>-</span><span>-</span>`;
    }
    bidsList.appendChild(li);
  }
}