<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="common/header.jsp" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내 지갑</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/wallet.css">

<!--  contextPath 설정 -->
<script>
	const contextPath = "${pageContext.request.contextPath}";
</script>
</head>
<body>
	<div class="wallet-container">
		<h2>내 지갑</h2>

		<!-- 잔액 정보 -->
		<div class="section-title">잔액 정보</div>
		<div class="balance-wrapper vertical">
			<div class="balance-row">
				<span class="label">평가 금액 (Total)</span> <span class="value">${wallet.totalValue}
					USDT</span>
			</div>
			<div class="balance-row">
				<span class="label">BTC 보유 수량</span> <span class="value">${String.format("%.8f", wallet.btcBalance)} BTC</span>
			</div>
			<div class="balance-row">
				<span class="label">주문 가능 금액</span> <span class="value">${String.format("%.8f", wallet.usdtBalance)}	USDT</span>
			</div>
		</div>
	</div>

	<!-- 수익률 -->
	<div class="profit-rate"
		style="color: ${wallet.profitRateValue >= 0 ? 'green' : 'red'};">
		수익률: ${wallet.profitRateSafe}%</div>

	<span class="value">평가 금액: ${wallet.totalValue} USDT</span>

<!-- 💰 충전 모달 열기 버튼 -->
<button id="openDepositModalBtn">충전하기</button>

	<!-- 💰 충전 모달 -->
	<div id="depositModal" class="modal">
		<div class="modal-content">
			<h3>시드머니 충전</h3>
			<input type="number" id="depositAmount"
				placeholder="100 ~ 1,000,000 USDT" />
			<div class="modal-buttons">
				<button onclick="submitDeposit()">충전</button>
				<button onclick="closeDepositModal()">닫기</button>
			</div>
		</div>
	</div>
	
	<!-- 시드머니/초기화 버튼 -->
<!-- 초기화 모달 열기 버튼 (모달 외부, 항상 보임) -->
<button id="openResetModalBtn">지갑 초기화</button>

<!-- 초기화 모달 -->
<div id="resetModal" class="reset-modal" style="display: none;">
  <div class="reset-modal-content">
    <h3>지갑 초기화</h3>
    <p>초기화를 진행하려면 아래에 "<strong>RESET</strong>"을 입력 해 주세요.</p>
    <input type="text" id="resetConfirmInput" placeholder="RESET 입력" />
    <div class="reset-modal-buttons">
      <button id="resetConfirmBtn" disabled>초기화</button>
      <button onclick="closeResetModal()">취소</button>
    </div>
  </div>
</div>


	<!-- 거래내역 보기 버튼 -->
	<a href="history" class="btn-history">내 거래내역 보기</a>
	</div>
	<script src="${pageContext.request.contextPath}/resources/js/wallet.js"></script>
</body>
</html>
