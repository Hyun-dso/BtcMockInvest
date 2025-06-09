const MAX_LEVELS = 6;

export function renderAsks(asks, tickSize = 0.01, currentPrice = 0) {
  const asksList = document.getElementById("asks");
  asksList.innerHTML = "";

  const aggregated = {};
  Object.entries(asks).forEach(([p, qty]) => {
    const price = parseFloat(p);
    const key = (Math.ceil(price / tickSize) * tickSize).toFixed(2);
    aggregated[key] = (aggregated[key] || 0) + parseFloat(qty);
  });

  const startPrice = Math.ceil(currentPrice / tickSize) * tickSize;

  for (let i = MAX_LEVELS; i > 0; i--) {
    const price = (startPrice + tickSize * i).toFixed(2);
    const qty = aggregated[price];  
    const li = document.createElement("li");
	if (qty) {
	  li.innerHTML = `<span>${parseFloat(price).toFixed(2)}</span><span>${qty.toFixed(5)} BTC</span>`;
	  li.addEventListener("click", () => {
	    if (window.handleOrderbookClick) window.handleOrderbookClick("ASK", parseFloat(price));
	  });
    } else {
      li.innerHTML = `<span>${parseFloat(price).toFixed(2)}</span><span>-</span>`;
    }
    asksList.appendChild(li);
  }
}