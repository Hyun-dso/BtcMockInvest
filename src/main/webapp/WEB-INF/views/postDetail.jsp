<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>${post.title}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
        }
        .post-box {
            border: 1px solid #ccc;
            padding: 20px;
            margin-bottom: 30px;
        }
        .comment-box {
            margin-bottom: 10px;
            padding: 10px;
            border-bottom: 1px solid #eee;
        }
        .comment-author {
            font-weight: bold;
        }
    </style>
</head>
<body>

    <div class="post-box">
        <h2>${post.title}</h2>
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
