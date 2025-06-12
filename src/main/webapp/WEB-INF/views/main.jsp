<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BtcMockInvest</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/theme.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/base.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/button.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/component.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/responsive.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
</head>
<body>
        <jsp:include page="common/header.jsp" />
        <div class="page-wrapper">
		<main class="main-content">
			<section class="hero-main">
				<div class="container">
					<h2>비트코인을 연습하세요, 진짜처럼.</h2>
					<p>실시간 시세 기반으로 안전하게 투자 연습을 시작해보세요.</p>
					<a href="${pageContext.request.contextPath}/signin"
						class="btn-primary">지금 시작하기</a>
				</div>
			</section>
			<section class="features">
				<div class="container">
					<div class="feature-grid">
						<div class="feature-box">
							<h3>실시간 시세</h3>
							<p>정확한 시세 데이터를 실시간으로 반영해요.</p>
						</div>
						<div class="feature-box">
							<h3>매수/매도 체험</h3>
							<p>가상 자산으로 실제처럼 거래해볼 수 있어요.</p>
						</div>
						<div class="feature-box">
							<h3>포트폴리오 추적</h3>
							<p>내 투자 기록을 한눈에 확인할 수 있어요.</p>
						</div>
					</div>
				</div>
			</section>
		</main>
		<footer class="footer">
			<div class="container">&copy; 2025 BtcMockInvest. All rights
				reserved.</div>
		</footer>
	</div>
</body>
</html>
