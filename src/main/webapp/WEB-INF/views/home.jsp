<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
</head>
<body>
<h3>이게 결과 페이지!</h3>
<p>현재 시간: ${now}입니다.</p>
<c:out value="JSTL도 사용 가능"/>
</body>
</html>