<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="common/header.jsp" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Main</title>
    <style>
        body, html {
            margin: 0;
            padding: 0;
            font-family: sans-serif;
            background: linear-gradient(to bottom, #000000, #2c2c2c); /* ✅ 어두운 그라데이션 배경 */
            color: white;
        }

        main {
            width: 100%;
            padding: 80px 0 40px;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .video-box {
            width: 80vw;
            max-width: 960px;
            aspect-ratio: 16/9;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: 0 0 30px rgba(0, 0, 0, 0.7);
        }

        .video-box video {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .content {
            padding: 80px 20px 120px;
            text-align: center;
        }

        .content h2 {
            margin-bottom: 20px;
            font-size: 2rem;
        }

        .content p {
            max-width: 700px;
            margin: 0 auto;
            line-height: 1.6;
            font-size: 1.1rem;
        }

        @media (max-width: 768px) {
            .video-box {
                width: 95vw;
            }

            .content h2 {
                font-size: 1.5rem;
            }

            .content p {
                font-size: 1rem;
            }
        }
    </style>
</head>
<body>
<header>
    <div class="logo-nav">
        <img src="<%= request.getContextPath() %>/resources/img/btc-logo.png" alt="Logo">
        <nav>
            <span class="nav-link">거래하기 ▾</span>
            <div class="dropdown">
                <a href="home">BTC</a>
            </div>
        </nav>
    </div>
    <div class="auth-buttons">
        <a href="signin">SIGNIN</a>
        <a href="regist">REGIST</a>
    </div>
</header>
<main>
    <div class="video-box">
        <video autoplay muted loop>
            <source src="${pageContext.request.contextPath}/resources/video/background01.mp4" type="video/mp4">
            Your browser does not support the video tag.
        </video>
    </div>
</main>

<div class="content">
    <h2>비트코인 모의투자 플랫폼</h2>
    <p>
        실시간 시세, 매수/매도 체험,<br>
        쉽고 빠르게 시작하세요.
    </p>
</div>

</body>
</html>
