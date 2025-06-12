<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>내 거래내역</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/button.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/component.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/responsive.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/history.css">
</head>
<body>
<jsp:include page="common/header.jsp" />
<div class="history-container">
    <h2>내 거래내역</h2>

    <table>
        <thead>
        <tr>
            <th>날짜</th>
            <th>종류</th>
            <th>수량</th>
            <th>거래가</th>
            <th>총 금액</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty history}">
                <c:forEach var="trade" items="${history}">
                    <tr>
                        <td>${trade.date}</td>
                        <td>${trade.type}</td>
                        <td>${trade.amount}</td>
                        <td>${trade.price}</td>
                        <td>${trade.total}</td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr><td colspan="5" class="no-data">거래내역이 없습니다.</td></tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>
</body>
</html>
