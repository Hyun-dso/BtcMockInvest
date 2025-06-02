<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="common/header.jsp" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Main</title>
    <!-- ✅ 외부 CSS 연결 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
</head>
<body>

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
