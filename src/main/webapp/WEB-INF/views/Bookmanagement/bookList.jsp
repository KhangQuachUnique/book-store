<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Book Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/adminBook.css">
</head>
<body>
<div class="container">
    <h1 class="page-title">Book Management</h1>

    <!-- Display error message if present -->
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

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
    <div id="categoryTable" class="modal" style="display: none;">
        <div class="card-content">
            <button type="button" class="btn btn-primary" onclick="submitFilterForm()">Submit</button>
            <div class="category-list">
                <c:forEach var="category" items="${categories}">
                    <c:set var="isInclude" value="${includeCategories != null && includeCategories.contains(category.id)}"/>
                    <c:set var="isExclude" value="${excludeCategories != null && excludeCategories.contains(category.id)}"/>
                    <div class="category-item ${isInclude ? 'include' : ''} ${isExclude ? 'exclude' : ''}"
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
                        <td><c:out value="${book.title}" default="N/A"/></td>
                        <td><c:out value="${book.author}" default="N/A"/></td>
                        <td><c:out value="${book.publisher}" default="N/A"/></td>
                        <td><c:out value="${book.category != null ? book.category.name : 'N/A'}" default="N/A"/></td>
                        <td><c:out value="${book.stock != null ? book.stock : '0'}" default="0"/></td>
                        <td><fmt:formatNumber value="${book.price != null ? book.price : 0}" type="currency" currencySymbol="$"/></td>
                        <td class="action-buttons">
                            <a href="${pageContext.request.contextPath}/admin/book?action=edit&id=${book.id}" class="btn btn-primary btn-sm">Edit</a>
                            <a href="${pageContext.request.contextPath}/admin/book?action=delete&id=${book.id}"
                               onclick="return confirm('Are you sure?')" class="btn btn-destructive btn-sm">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty books}">
                    <tr>
                        <td colspan="8">No books found.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>

            <!-- Phân trang -->
            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <c:url var="prevUrl" value="/admin/book">
                        <c:param name="action" value="filter"/>
                        <c:param name="page" value="${currentPage - 1}"/>
                        <c:if test="${not empty title}">
                            <c:param name="title" value="${title}"/>
                        </c:if>
                        <c:if test="${not empty publishYear}">
                            <c:param name="publish_year" value="${publishYear}"/>
                        </c:if>
                        <c:if test="${not empty includeCategories}">
                            <c:forEach var="cat" items="${includeCategories}">
                                <c:param name="includeCategories" value="${cat}"/>
                            </c:forEach>
                        </c:if>
                        <c:if test="${not empty excludeCategories}">
                            <c:forEach var="cat" items="${excludeCategories}">
                                <c:param name="excludeCategories" value="${cat}"/>
                            </c:forEach>
                        </c:if>
                    </c:url>
                    <c:choose>
                        <c:when test="${currentPage > 1}">
                            <a href="${prevUrl}" class="btn btn-primary">&lt;</a>
                        </c:when>
                        <c:otherwise>
                            <span class="btn btn-primary disabled">&lt;</span>
                        </c:otherwise>
                    </c:choose>

                    <c:forEach begin="1" end="${totalPages}" var="pageNum">
                        <c:url var="pageUrl" value="/admin/book">
                            <c:param name="action" value="filter"/>
                            <c:param name="page" value="${pageNum}"/>
                            <c:if test="${not empty title}">
                                <c:param name="title" value="${title}"/>
                            </c:if>
                            <c:if test="${not empty publishYear}">
                                <c:param name="publish_year" value="${publishYear}"/>
                            </c:if>
                            <c:if test="${not empty includeCategories}">
                                <c:forEach var="cat" items="${includeCategories}">
                                    <c:param name="includeCategories" value="${cat}"/>
                                </c:forEach>
                            </c:if>
                            <c:if test="${not empty excludeCategories}">
                                <c:forEach var="cat" items="${excludeCategories}">
                                    <c:param name="excludeCategories" value="${cat}"/>
                                </c:forEach>
                            </c:if>
                        </c:url>
                        <c:choose>
                            <c:when test="${pageNum == currentPage}">
                                <span class="btn btn-primary active">${pageNum}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageUrl}" class="btn btn-primary">${pageNum}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:url var="nextUrl" value="/admin/book">
                        <c:param name="action" value="filter"/>
                        <c:param name="page" value="${currentPage + 1}"/>
                        <c:if test="${not empty title}">
                            <c:param name="title" value="${title}"/>
                        </c:if>
                        <c:if test="${not empty publishYear}">
                            <c:param name="publish_year" value="${publishYear}"/>
                        </c:if>
                        <c:if test="${not empty includeCategories}">
                            <c:forEach var="cat" items="${includeCategories}">
                                <c:param name="includeCategories" value="${cat}"/>
                            </c:forEach>
                        </c:if>
                        <c:if test="${not empty excludeCategories}">
                            <c:forEach var="cat" items="${excludeCategories}">
                                <c:param name="excludeCategories" value="${cat}"/>
                            </c:forEach>
                        </c:if>
                    </c:url>
                    <c:choose>
                        <c:when test="${currentPage < totalPages}">
                            <a href="${nextUrl}" class="btn btn-primary">&gt;</a>
                        </c:when>
                        <c:otherwise>
                            <span class="btn btn-primary disabled">&gt;</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </div>
    </div>
</div>

<script>
    // Initialize category arrays from server-side data
    let includeCategories = ${includeCategories != null ? includeCategories : '[]'};
    let excludeCategories = ${excludeCategories != null ? excludeCategories : '[]'};

    function toggleCategoryTable() {
        const table = document.getElementById('categoryTable');
        table.style.display = table.style.display === 'none' ? 'block' : 'none';
    }

    function toggleCategory(element, categoryId) {
        // Remove existing classes
        element.classList.remove('include', 'exclude');

        // Check current state and update arrays
        if (includeCategories.includes(categoryId)) {
            includeCategories = includeCategories.filter(id => id !== categoryId);
            excludeCategories.push(categoryId);
            element.classList.add('exclude');
        } else if (excludeCategories.includes(categoryId)) {
            excludeCategories = excludeCategories.filter(id => id !== categoryId);
        } else {
            includeCategories.push(categoryId);
            element.classList.add('include');
        }
    }

    function submitFilterForm() {
        document.getElementById('includeCategories').value = includeCategories.join(',');
        document.getElementById('excludeCategories').value = excludeCategories.join(',');
        document.querySelector('.search-form').submit();
    }
</script>
</body>
</html>