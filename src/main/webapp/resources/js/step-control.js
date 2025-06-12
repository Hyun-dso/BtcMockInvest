document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".step-up").forEach(btn => {
    btn.addEventListener("click", () => {
      const targetId = btn.getAttribute("data-target");
      const input = document.getElementById(targetId);
      const step = parseFloat(input.dataset.step) || 0.1;
      const current = parseFloat(input.value) || 0;
      const result = current + step;
      input.value = result.toFixed(step < 1 ? 2 : 0);
    });
  });

  document.querySelectorAll(".step-down").forEach(btn => {
    btn.addEventListener("click", () => {
      const targetId = btn.getAttribute("data-target");
      const input = document.getElementById(targetId);
      const step = parseFloat(input.dataset.step) || 0.1;
      const current = parseFloat(input.value) || 0;
      const result = current - step;

      if (result < 0) return;

      input.value = result.toFixed(step < 1 ? 2 : 0);
    });
  });
});