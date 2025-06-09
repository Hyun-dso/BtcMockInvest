<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="common/header.jsp" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내 거래내역</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/history.css">
</head>
<body>
	<div class="history-container">
		<h2>내 거래내역</h2>

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
					<c:when test="${not empty history}">
						<c:forEach var="trade" items="${history}">
							<tr>
								<td><fmt:formatDate value="${trade.createdAtDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td class="${trade.userType eq 'BUY' ? 'buy' : 'sell'}"><c:choose>
										<c:when test="${trade.userType eq 'BUY'}">매수</c:when>
										<c:otherwise>매도</c:otherwise>
									</c:choose></td>
								<td class="${trade.userType eq 'BUY' ? 'buy' : 'sell'}"><c:choose>
										<c:when test="${trade.userType eq 'BUY'}">+</c:when>
										<c:otherwise>-</c:otherwise>
									</c:choose> <fmt:formatNumber value="${trade.amount}" pattern="0.#####" />
								</td>
								<td><fmt:formatNumber value="${trade.price}" pattern="0.00" /></td>
								<td class="${trade.userType eq 'BUY' ? 'sell' : 'buy'}"><c:choose>
										<c:when test="${trade.userType eq 'BUY'}">-</c:when>
										<c:otherwise>+</c:otherwise>
									</c:choose> <fmt:formatNumber value="${trade.total}" pattern="0.00" /></td>
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
	</div>
</body>
</html>
