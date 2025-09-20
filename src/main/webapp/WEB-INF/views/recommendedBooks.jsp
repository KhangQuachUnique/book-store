<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
    <div class="recommended-books-container">
        <p>You may also like</p>
        <div class="recommended-books">
            <c:forEach var="book" items="${recommendedBooks}">
                <div class="book-card">
                    <img src="${book.thumbnailUrl}" alt="${book.title}">
                    <p>Title: ${book.title}</p>
                    <p>Author: ${book.author}</p>
                    <p>Price: $${book.price}</p>
                    <p>Rating: ${book.rating}</p>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>
