function setupTradePagination() {
	const tbody = document.querySelector('.history-container tbody');
	if (!tbody) return;
	const allRows = Array.from(tbody.querySelectorAll('tr'));
	const rows = allRows.filter(tr => !tr.querySelector('.no-data'));
	const rowsPerPage = 13;
	if (rows.length <= rowsPerPage) return;

	const pagination = document.querySelector('.history-container .pagination');
	if (!pagination) return;

	let currentPage = 1;
	const pageCount = Math.ceil(rows.length / rowsPerPage);

	function renderPagination() {
		pagination.innerHTML = '';
		for (let i = 1; i <= pageCount; i++) {
			const btn = document.createElement('button');
			btn.textContent = i;
			if (i === currentPage) btn.classList.add('active');
			btn.addEventListener('click', () => {
				currentPage = i;
				update();
			});
			pagination.appendChild(btn);
		}
	}

	function update() {
		rows.forEach((row, idx) => {
			row.style.display = (idx >= (currentPage - 1) * rowsPerPage && idx < currentPage * rowsPerPage) ? '' : 'none';
		});
		renderPagination();
	}

	update();
}

// 탭이 '거래내역'으로 전환될 때 실행
document.addEventListener('DOMContentLoaded', () => {
	const tabButtons = document.querySelectorAll('.tab-btn');
	tabButtons.forEach(btn => {
		btn.addEventListener('click', () => {
			if (btn.dataset.tab === 'history') {
				setupTradePagination();
			}
		});
	});
	const active = document.querySelector('.tab-btn.active');
	if (active && active.dataset.tab === 'history') {
		setupTradePagination();
	}
});
