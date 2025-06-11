document.addEventListener('DOMContentLoaded', () => {
  const tbody = document.querySelector('.history-container tbody');
  if (!tbody) return;
  const allRows = Array.from(tbody.querySelectorAll('tr'));
  const rows = allRows.filter(tr => !tr.querySelector('.no-data'));
  const rowsPerPage = 15;
  if (rows.length <= rowsPerPage) return; // no pagination needed

  const pagination = document.createElement('div');
  pagination.className = 'pagination';
  tbody.parentElement.after(pagination);

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
      if (idx >= (currentPage - 1) * rowsPerPage && idx < currentPage * rowsPerPage) {
        row.style.display = '';
      } else {
        row.style.display = 'none';
      }
    });
    renderPagination();
  }

  update();
});