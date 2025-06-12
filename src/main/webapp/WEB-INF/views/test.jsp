<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Input Demo</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/input-style.css">
</head>
<body>
	<div class="trade-inputs">
		<div class="input-row">
			<div class="input-wrapper">
				<span class="floating-label">Price</span> <input type="number"
					id="price" data-step="1" />
				<div class="unit-group">
					<span class="unit">USDT</span>
					<div class="step">
						<button type="button" class="step-up" data-target="price">▲</button>
						<button type="button" class="step-down" data-target="price">▼</button>
					</div>
				</div>
			</div>
		</div>

		<div class="input-row">
			<div class="input-wrapper">
				<span class="floating-label">Amount</span> <input type="number"
					id="amount" data-step="0.1" />
				<div class="unit-group">
					<span class="unit">BTC</span>
					<div class="step">
						<button type="button" class="step-up" data-target="amount">▲</button>
						<button type="button" class="step-down" data-target="amount">▼</button>
					</div>
				</div>
			</div>
		</div>
	</div>
<script src="${pageContext.request.contextPath}/resources/js/step-control.js"></script>
</body>
</html>