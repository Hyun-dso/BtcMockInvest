const MAX_LEVELS = 6;

export function renderBids(bids, tickSize = 0.01, currentPrice = 0) {
  const bidsList = document.getElementById("bids");
  bidsList.innerHTML = "";

  const aggregated = {};
  Object.entries(bids).forEach(([p, qty]) => {
    const price = parseFloat(p);
    const key = (Math.floor(price / tickSize) * tickSize).toFixed(2);
    aggregated[key] = (aggregated[key] || 0) + parseFloat(qty);
  });

	//     .sort((a, b) => parseFloat(a[0]) - parseFloat(b[0])) // 오름차순
	const startPrice = Math.floor(currentPrice / tickSize) * tickSize;

  for (let i = 0; i < MAX_LEVELS; i++) {
	const price = (startPrice - tickSize * (i + 1)).toFixed(2);
	const qty = aggregated[price];
    const li = document.createElement("li");
	if (qty) {
	  li.innerHTML = `<span>${parseFloat(price).toFixed(2)}</span><span>${qty.toFixed(5)} BTC</span>`;
	  li.addEventListener("click", () => {
	    if (window.handleOrderbookClick) window.handleOrderbookClick("BID", parseFloat(price));
	  });
    } else {
      li.innerHTML = `<span>${parseFloat(price).toFixed(2)}</span><span>-</span>`;
    }
    bidsList.appendChild(li);
  }
}