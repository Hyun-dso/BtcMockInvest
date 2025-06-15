<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="common/header.jsp" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Page</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/mypage.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/history.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/wallet.css">
<script src="${pageContext.request.contextPath}/resources/js/mypage.js"
	defer></script>
<script
	src="${pageContext.request.contextPath}/resources/js/trade-history.js"
	defer></script>
<script src="${pageContext.request.contextPath}/resources/js/wallet.js"
	defer></script>
<script>
	const contextPath = "${pageContext.request.contextPath}";
</script>
</head>
<body>
	<div class="mypage-container">
		<h2 class="page-title">마이페이지</h2>

		<div class="mypage-wrapper">
			<div class="left-contianer">
				<div class="profile-box">
					<div class="profile-circle">
						<c:out value="${loginUser.username.substring(0, 1)}" />
					</div>
					<div class="profile-info">
						<div class="profile-name">${loginUser.username}</div>
						<div class="profile-email">${loginUser.email}</div>
					</div>
				</div>

				<div class="menu-list">
					<button class="tab-btn active" data-tab="profile">👤 내 정보</button>
					<button class="tab-btn" data-tab="wallet">💰 내 지갑</button>
					<button class="tab-btn" data-tab="history">🧾 거래내역</button>
					<button class="tab-btn" data-tab="username">✏️ 닉네임 변경</button>
					<button class="tab-btn" data-tab="password">🔒 비밀번호 변경</button>
				</div>
			</div>
			<div class="mid-container">
				<div class="tab-content-wrapper">
					<div id="profile" class="tab-content active">
						<!-- 프로필 표시 -->
						<p>사용자 이름 : ${loginUser.username}</p>
						<p>이메일 : ${loginUser.email}</p>
						<p>회원 가입 날짜: ${loginUser.createdAt}</p>
						<p>마지막 접속 시간: ${loginUser.lastLoginAt}</p>
					</div>
					<div id="wallet" class="tab-content">
						<div class="balance-wrapper vertical">
							<div class="balance-row">
								<span class="label">평가 금액 (Total)</span> <span class="value">${wallet.totalValue}
									USDT</span>
							</div>
							<div class="balance-row">
								<span class="label">BTC 보유 수량</span> <span class="value">${String.format("%.8f", wallet.btcBalance)}
									BTC</span>
							</div>
							<div class="balance-row">
								<span class="label">주문 가능 금액</span> <span class="value"
									style="margin-left: auto;"> ${String.format("%.2f", wallet.usdtBalance)}
									USDT </span>
							</div>
							<div class="balance-row">
								<span class="label">시작 금액</span> <span class="value"> <fmt:formatNumber
										value="${wallet.initialValue}" pattern="0.00" /> USDT
								</span>
							</div>
							<div class="profit-rate"
								style="margin-top: 10px; color: ${wallet.profitRateValue >= 0 ? '#00b386' : '#ff4d4f'};">
								수익률: ${wallet.profitRateSafe}%</div>
						</div>
						<div class="wallet-buttons">
							<button id="openDepositModalBtn">충전</button>
							<button id="openResetModalBtn">초기화</button>
						</div>
						<!-- 충전 모달 -->
						<div id="depositModal" class="modal">
							<div class="modal-content">
								<h3>충전 금액 입력</h3>
								<input type="number" id="depositAmount"
									placeholder="금액 (100 ~ 1,000,000 USDT)" />
								<div class="modal-buttons">
									<button onclick="submitDeposit()">충전</button>
									<button onclick="closeDepositModal()">취소</button>
								</div>
							</div>
						</div>
						<!-- 초기화 모달 -->
						<div id="resetModal" class="reset-modal" style="display: none;">
							<div class="reset-modal-content">
								<h3>지갑 초기화</h3>
								<p>"RESET"을 입력해야 초기화가 진행됩니다.</p>
								<input type="text" id="resetConfirmInput" placeholder="RESET 입력" />
								<div class="reset-modal-buttons">
									<button onclick="window.closeResetModal()">취소</button>
									<button id="resetConfirmBtn" disabled>초기화</button>
								</div>
							</div>
						</div>
					</div>

					<div id="history" class="tab-content">
						<table>
							<thead>
								<tr>
									<th>날짜</th>
									<th>종류</th>
									<th>수량</th>
									<th>체결가(BTC)</th>
									<th>주문 금액(USDT)</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${not empty tradeHistory}">
										<c:forEach var="trade" items="${tradeHistory}">
											<tr>
												<td><fmt:formatDate value="${trade.createdAtDate}"
														pattern="yyyy-MM-dd HH:mm:ss" /></td>
												<td class="${trade.userType eq 'BUY' ? 'buy' : 'sell'}">
													<c:choose>
														<c:when test="${trade.userType eq 'BUY'}">매수</c:when>
														<c:otherwise>매도</c:otherwise>
													</c:choose>
												</td>
												<td class="${trade.userType eq 'BUY' ? 'buy' : 'sell'}">
													<c:choose>
														<c:when test="${trade.userType eq 'BUY'}">+</c:when>
														<c:otherwise>-</c:otherwise>
													</c:choose> <fmt:formatNumber value="${trade.amount}"
														pattern="0.#####" />
												</td>
												<td><fmt:formatNumber value="${trade.price}"
														pattern="0.00" /></td>
												<td class="${trade.userType eq 'BUY' ? 'sell' : 'buy'}">
													<c:choose>
														<c:when test="${trade.userType eq 'BUY'}">-</c:when>
														<c:otherwise>+</c:otherwise>
													</c:choose> <fmt:formatNumber value="${trade.total}" pattern="0.00" />
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="5" class="no-data">거래내역이 없습니다.</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<div class="pagination"></div>
					</div>
				</div>
				<div id="username" class="tab-content">
					<!-- 유저네임 변경 폼 -->
				</div>
				<div id="password" class="tab-content">
					<!-- 비밀번호 변경 폼 -->
				</div>
			</div>
		</div>
	</div>
</body>
</html>
