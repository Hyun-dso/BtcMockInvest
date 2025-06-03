<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--  세션 유지 확인용 jsp입니다!! -->

<html>
<body>
	<c:if test="${not empty sessionScope.loginUser}">
    <p>안녕하세요, ${sessionScope.loginUser.username}님</p>
    <a href="${pageContext.request.contextPath}/logout">로그아웃</a>
</c:if>
<c:if test="${empty sessionScope.loginUser}">
    <a href="${pageContext.request.contextPath}/signin">로그인</a> |
    <a href="${pageContext.request.contextPath}/signup">회원가입</a>
</c:if>
</body>
</html>
