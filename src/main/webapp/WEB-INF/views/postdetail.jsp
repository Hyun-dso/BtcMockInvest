<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>chat</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/postDetail.css">
</head>
<body>

    <div class="post-box">
        <h2>${post.username}</h2>
        <p>${post.content}</p>
    </div>

    <h3>댓글</h3>
    <c:if test="${empty commentList}">
        <p>댓글이 없습니다.</p>
    </c:if>

    <c:forEach var="comment" items="${commentList}">
        <div class="comment-box">
            <span class="comment-author">${comment.author}</span>: ${comment.content}
        </div>
    </c:forEach>

</body>
</html>
