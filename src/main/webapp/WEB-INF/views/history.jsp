<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="common/header.jsp" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>내 거래내역</title>
    <style>
        .history-container {
            max-width: 900px;
            margin: 100px auto;
            padding: 30px;
            background: #f4f4f4;
            border: 1px solid #ccc;
            border-radius: 10px;
        }

        h2 {
            text-align: center;
            margin-bottom: 30px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            box-shadow: 0 0 5px rgba(0,0,0,0.1);
        }

        th, td {
            padding: 12px;
            text-align: center;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #f0f0f0;
        }

        tr:hover {
            background-color: #fafafa;
        }

        .no-data {
            text-align: center;
            padding: 20px;
            color: #999;
        }
    </style>
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
