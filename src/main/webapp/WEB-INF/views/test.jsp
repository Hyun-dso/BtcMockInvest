<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--  세션 유지 확인용 jsp입니다!! -->
<!DOCTYPE html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/slider.css">
</head>
<html>
<body>
    <input type="range" min="0" max="100" value="0" class="binance-slider">
    <div class="slider-markers">
        <div class="marker" style="left: 0%;"></div>
        <div class="marker" style="left: 25%;"></div>
        <div class="marker" style="left: 50%;"></div>
        <div class="marker" style="left: 75%;"></div>
        <div class="marker" style="left: 100%;"></div>
    </div>
</body>
</html>
