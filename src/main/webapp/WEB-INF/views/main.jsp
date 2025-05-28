<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="common/header.jsp" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Main</title>
    <style>
        body {
            margin: 0;
            font-family: sans-serif;
        }

        main {
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(to right, #000000, #2c003e); /* 검정 + 어두운보라 */
            padding-top: 80px;
        }

        .hero {
            text-align: center;
            animation: fadeIn 2s ease-in-out;
        }

        .hero h1 {
            font-size: 64px;
            font-weight: bold;
            color: #f2a900; /* BTC 컬러 */
            text-shadow: 2px 2px 6px rgba(0, 0, 0, 0.3);
            margin-bottom: 20px;
        }

        .hero p {
            font-size: 20px;
            color: #ccc;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
    </style>
</head>
<body>

<main>
    <div class="hero">
        <h1>BTC MOCK INVEST</h1>
        <p>비트코인 가상투자를 경험해보세요.</p>
    </div>
</main>

</body>
</html>
