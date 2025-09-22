<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Book Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/adminBook.css">
</head>
<body>
<div class="container">
    <h1 class="page-title">Book Management</h1>

    <!-- Thanh tìm kiếm -->
    <div class="card">
        <div class="card-content">
            <form action="${pageContext.request.contextPath}/admin/book" method="get" class="search-form">
                <input type="hidden" name="action" value="filter">
                <div class="form-group">
                    <input type="text" name="title" placeholder="Search by title" value="${title}" class="input">
                </div>
                <div class="form-group">
                    <input type="number" name="publish_year" placeholder="Publish year" value="${publishYear}" class="input">
                </div>
                <div class="form-group">
                    <button type="button" class="btn btn-primary" onclick="toggleCategoryTable()">Select Categories</button>
                </div>
                <div class="form-group">
                    <input type="submit" value="Find" class="btn btn-primary">
                </div>
                <input type="hidden" name="includeCategories" id="includeCategories" value="${includeCategories}">
                <input type="hidden" name="excludeCategories" id="excludeCategories" value="${excludeCategories}">
            </form>
        </div>
    </div>

    <!-- Bảng category -->
    <div id="categoryTable" class="modal">
        <div class="card-content">
            <button type="button" class="btn btn-primary" onclick="submitFilterForm()">Submit</button>
            <div class="category-list">
                <c:forEach var="category" items="${categories}">
                    <div class="category-item"
                         data-id="${category.id}"
                         onclick="toggleCategory(this, ${category.id})">
                            ${category.name}
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <!-- Nút All Books và Add Book -->
    <div class="nav-actions">
        <a href="${pageContext.request.contextPath}/admin/book?action=list" class="btn btn-primary">All Books</a>
        <a href="${pageContext.request.contextPath}/admin/book?action=add" class="btn btn-primary">Add Book</a>
    </div>

    <!-- Danh sách sách -->
    <div class="card">
        <div class="card-content">
            <table class="table">
                <thead class="table-header">
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Publisher</th>
                    <th>Category</th>
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
                            <a href="${pageContext.request.contextPath}/admin/book?action=edit&id=${book.id}" class="btn btn-primary btn-sm">Edit</a>
                            <a href="${pageContext.request.contextPath}/admin/book?action=delete&id=${book.id}"
                               onclick="return confirm('Are you sure?')" class="btn btn-destructive btn-sm">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <!-- Phân trang -->
            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <c:choose>
                        <c:when test="${currentPage > 1}">
                            <a href="${pageContext.request.contextPath}/admin/book?action=list&page=${currentPage - 1}" class="btn btn-primary">Previous</a>
                        </c:when>
                        <c:otherwise>
                            <span class="btn btn-primary" style="opacity: 0.5; cursor: not-allowed;">Previous</span>
                        </c:otherwise>
                    </c:choose>
                    <input type="number" min="1" max="${totalPages}" value="${currentPage}"
                           class="page-input"
                           onkeypress="if(event.key === 'Enter' && this.value >= 1 && this.value <= ${totalPages}) { window.location.href = '${pageContext.request.contextPath}/admin/book?action=list&page=' + this.value; }">
                    <span>of ${totalPages}</span>
                    <c:choose>
                        <c:when test="${currentPage < totalPages}">
                            <a href="${pageContext.request.contextPath}/admin/book?action=list&page=${currentPage + 1}" class="btn btn-primary">Next</a>
                        </c:when>
                        <c:otherwise>
                            <span class="btn btn-primary" style="opacity: 0.5; cursor: not-allowed;">Next</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
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
            includeCategories = includeCategories.filter(id => id !== categoryId);
            if (!excludeCategories.includes(categoryId)) {
                excludeCategories.push(categoryId);
            }
        } else if (element.classList.contains('exclude')) {
            element.classList.remove('exclude');
            excludeCategories = excludeCategories.filter(id => id !== categoryId);
        } else {
            element.classList.add('include');
            if (!includeCategories.includes(categoryId)) {
                includeCategories.push(categoryId);
            }
        }
    }

    function submitFilterForm() {
        document.getElementById('includeCategories').value = includeCategories.length > 0 ? includeCategories.join(',') : '';
        document.getElementById('excludeCategories').value = excludeCategories.length > 0 ? excludeCategories.join(',') : '';
        document.querySelector('.search-form').submit();
    }
</script>
</body>
</html>