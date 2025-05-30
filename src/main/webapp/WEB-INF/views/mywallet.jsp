<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="common/header.jsp" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>내 지갑</title>
    <style>
        .wallet-container {
            max-width: 800px;
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

        .section-title {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 15px;
        }

        /* 잔액 정보 박스 (세로 정렬) */
        .balance-wrapper.vertical {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 0 5px rgba(0,0,0,0.1);
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .balance-row {
            display: flex;
            justify-content: space-between;
            font-size: 16px;
            border-bottom: 1px solid #eee;
            padding-bottom: 8px;
        }

        .balance-row:last-child {
            border-bottom: none;
        }

        .label {
            font-weight: bold;
            color: #555;
        }

        .value {
            color: #222;
        }

        /* 수익률 */
        .profit-rate {
            margin-top: 30px;
            font-size: 16px;
            color: ${wallet.profitRate >= 0 ? 'green' : 'red'};
            text-align: left;
        }

        /* 버튼 영역 */
        .btns {
            margin-top: 30px;
            display: flex;
            justify-content: space-between;
            gap: 10px;
        }

        .btns form {
            flex: 1;
        }

        .btns button {
            width: 100%;
            padding: 10px;
            border: none;
            border-radius: 5px;
            background: #333;
            color: white;
            font-weight: bold;
            cursor: pointer;
        }

        .btns button:hover {
            background: #555;
        }

        /* 거래내역 버튼 */
        .btn-history {
            display: block;
            width: 100%;
            margin-top: 40px;
            padding: 12px;
            background: #007bff;
            color: white;
            text-align: center;
            text-decoration: none;
            border-radius: 6px;
            font-weight: bold;
        }

        .btn-history:hover {
            background: #0056b3;
        }
    </style>
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
    <div class="profit-rate">
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
