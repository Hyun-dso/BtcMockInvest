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
	  const price = parseFloat(p);
	  const amount = parseFloat(qty);
	  if (isNaN(price) || isNaN(amount)) {
	    li.innerHTML = `<span>-</span><span>-</span>`;
	  } else {
	    li.innerHTML = `<span>${price.toFixed(2)}</span><span>${amount.toFixed(5)} BTC</span>`;
	    li.addEventListener("click", () => {
	      if (window.handleOrderbookClick) window.handleOrderbookClick("ASK", price);
	    });
	  }
    } else {
      li.innerHTML = `<span>-</span><span>-</span>`;
    }
    asksList.appendChild(li);
  }
}