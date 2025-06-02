<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="common/header.jsp" />

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>내 지갑</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/wallet.css">
</head>
<body>
<div class="wallet-container">
	<h2>내 지갑</h2>

	<!-- 잔액 정보 -->
	<div class="section-title">잔액 정보</div>
	<div class="balance-wrapper vertical">
		<div class="balance-row">
			<span class="label">평가 금액 (Total)</span>
			<span class="value">${wallet.totalValue} USDT</span>
		</div>
		<div class="balance-row">
			<span class="label">BTC 보유 수량</span>
			<span class="value">${wallet.btcBalance} BTC</span>
		</div>
		<div class="balance-row">
			<span class="label">주문 가능 금액</span>
			<span class="value">${wallet.usdtBalance} USDT</span>
		</div>
	</div>

	<!-- 수익률 -->
	<div class="profit-rate" style="color: ${wallet.profitRate >= 0 ? 'green' : 'red'};">
		수익률: ${wallet.profitRate}%
	</div>

	<!-- 시드머니/초기화 버튼 -->
	<div class="btns">
		<form action="wallet/charge" method="post">
			<button type="submit">시드머니 충전</button>
		</form>
		<form action="wallet/reset" method="post">
			<button type="submit">초기화</button>
		</form>
	</div>

	<!-- 거래내역 보기 버튼 -->
	<a href="history" class="btn-history">내 거래내역 보기</a>
</div>
</body>
</html>
