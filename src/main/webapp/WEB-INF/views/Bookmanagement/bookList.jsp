<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Book Management - ${listType}</title>
    <link rel="stylesheet" href="/assets/styles/users.css">
    <style>
        .category-table { display: none; position: absolute; background: white; border: 1px solid #ccc; padding: 10px; z-index: 1000; }
        .category-table.show { display: block; }
        .category-item { padding: 5px; cursor: pointer; margin: 2px; }
        .category-item.include { border: 2px solid green; }
        .category-item.exclude { border: 2px solid black; }
    </style>
</head>
<body class="bg-background text-foreground">
<div class="container">
    <h1 class="page-title">${listType}</h1>

    <nav class="nav-main">
        <a href="/admin/book?action=list" class="nav-link">All Books</a>
    </nav>

    <nav class="nav-admin">
        <a href="/admin/book?action=add" class="btn btn-secondary">Add Book</a>
        <form action="/admin/book" method="post" enctype="multipart/form-data" style="display:inline;">
            <input type="hidden" name="action" value="importCSV">
            <input type="file" name="csvFile" accept=".csv" class="input">
            <input type="submit" value="Import CSV" class="btn btn-secondary">
        </form>
    </nav>

    <div class="card search-card">
        <div class="card-header">
            <h2 class="card-title">Filter Books</h2>
        </div>
        <div class="card-content">
            <form action="/admin/book" method="get" class="search-form">
                <input type="hidden" name="action" value="filter">
                <input type="text" name="title" class="input" placeholder="Search by Title" value="${title}">
                <input type="number" name="publishYear" class="input" placeholder="Publish Year" value="${publishYear}">
                <button type="button" class="btn btn-primary" onclick="toggleCategoryTable()">Select Categories</button>
                <div id="categoryTable" class="category-table">
                    <c:forEach var="category" items="${categories}">
                        <div class="category-item"
                             data-id="${category.id}"
                             onclick="toggleCategory(this, ${category.id})">
                                ${category.name}
                        </div>
                    </c:forEach>
                </div>
                <input type="hidden" name="includeCategories" id="includeCategories">
                <input type="hidden" name="excludeCategories" id="excludeCategories">
                <input type="submit" value="Filter" class="btn btn-primary">
            </form>
        </div>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>

    <div class="card">
        <div class="card-content">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/admin/book?action=${param.action}&page=${currentPage - 1}&title=${title}&publishYear=${publishYear}&includeCategories=${includeCategories}&excludeCategories=${excludeCategories}" class="btn btn-secondary">Previous</a>
                </c:if>
                <span>Page ${currentPage} of ${totalPages}</span>
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/admin/book?action=${param.action}&page=${currentPage + 1}&title=${title}&publishYear=${publishYear}&includeCategories=${includeCategories}&excludeCategories=${excludeCategories}" class="btn btn-secondary">Next</a>
                </c:if>
            </div>
            <table class="table">
                <thead>
                <tr class="table-header">
                    <th>ID</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Publisher</th>
                    <th>Category ID</th>
                    <th>Stock</th>
                    <th>Price</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="book" items="${books}">
                    <tr>
                        <td>${book.id}</td>
                        <td>${book.title}</td>
                        <td>${book.author}</td>
                        <td>${book.publisher}</td>
                        <td>${book.categoryId}</td>
                        <td>${book.stock}</td>
                        <td>${book.price}</td>
                        <td class="action-buttons">
                            <a href="${pageContext.request.contextPath}/admin/book?action=view&id=${book.id}" class="btn btn-outline btn-sm">View</a>
                            <a href="${pageContext.request.contextPath}/admin/book?action=edit&id=${book.id}" class="btn btn-outline btn-sm">Edit</a>
                            <form action="/admin/book" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${book.id}">
                                <input type="submit" value="Delete" class="btn btn-destructive btn-sm btn-lowe"
                                       onclick="return confirm('Are you sure you want to delete this book?')">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/admin/book?action=${param.action}&page=${currentPage - 1}&title=${title}&publishYear=${publishYear}&includeCategories=${includeCategories}&excludeCategories=${excludeCategories}" class="btn btn-secondary">Previous</a>
                </c:if>
                <span>Page ${currentPage} of ${totalPages}</span>
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/admin/book?action=${param.action}&page=${currentPage + 1}&title=${title}&publishYear=${publishYear}&includeCategories=${includeCategories}&excludeCategories=${excludeCategories}" class="btn btn-secondary">Next</a>
                </c:if>
            </div>
        </div>
    </div>
</div>

<script>
    let includeCategories = ${includeCategories != null ? includeCategories : '[]'};
    let excludeCategories = ${excludeCategories != null ? excludeCategories : '[]'};

    function toggleCategoryTable() {
        const table = document.getElementById('categoryTable');
        table.classList.toggle('show');
    }

    function toggleCategory(element, categoryId) {
        if (element.classList.contains('include')) {
            element.classList.remove('include');
            element.classList.add('exclude');
            includeCategories = includeCategories.filter(id => id != categoryId);
            excludeCategories.push(categoryId);
        } else if (element.classList.contains('exclude')) {
            element.classList.remove('exclude');
            excludeCategories = excludeCategories.filter(id => id != categoryId);
        } else {
            element.classList.add('include');
            includeCategories.push(categoryId);
        }
        document.getElementById('includeCategories').value = includeCategories.join(',');
        document.getElementById('excludeCategories').value = excludeCategories.join(',');
    }

    // Initialize category states
    document.querySelectorAll('.category-item').forEach(item => {
        const id = parseInt(item.getAttribute('data-id'));
        if (includeCategories.includes(id)) {
            item.classList.add('include');
        } else if (excludeCategories.includes(id)) {
            item.classList.add('exclude');
        }
    });
</script>
</body>
</html>