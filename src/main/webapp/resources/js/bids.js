const MAX_LEVELS = 6;

export function renderBids(bids) {
  const bidsList = document.getElementById("bids");
  bidsList.innerHTML = "";

  const entries = Object.entries(bids)
    .sort((a, b) => parseFloat(a[0]) - parseFloat(b[0])) // 오름차순
    .reverse()
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