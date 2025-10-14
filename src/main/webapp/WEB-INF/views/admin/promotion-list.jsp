<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="admin-container">
    <div class="admin-header" style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;">
        <h2 style="margin:0;">Promotions</h2>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/promotions?action=add">+ New Promotion</a>
    </div>

    <c:if test="${not empty flash_success}">
        <div class="alert alert-success">${flash_success}</div>
    </c:if>
    <c:if test="${not empty flash_error}">
        <div class="alert alert-error">${flash_error}</div>
    </c:if>

    <div class="card">
        <table class="table" style="width:100%;border-collapse:collapse;">
            <thead>
            <tr>
                <th style="text-align:left;padding:10px;border-bottom:1px solid #e5e7eb;">ID</th>
                <th style="text-align:left;padding:10px;border-bottom:1px solid #e5e7eb;">Code</th>
                <th style="text-align:left;padding:10px;border-bottom:1px solid #e5e7eb;">Discount (%)</th>
                <th style="text-align:left;padding:10px;border-bottom:1px solid #e5e7eb;">Expire At</th>
                <th style="text-align:right;padding:10px;border-bottom:1px solid #e5e7eb;">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty promotions}">
                    <tr>
                        <td colspan="5" style="padding:16px;text-align:center;color:#6b7280;">No promotions found.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="p" items="${promotions}">
                        <tr>
                            <td style="padding:10px;border-bottom:1px solid #f3f4f6;">${p.id}</td>
                            <td style="padding:10px;border-bottom:1px solid #f3f4f6;">${p.code}</td>
                            <td style="padding:10px;border-bottom:1px solid #f3f4f6;">${p.discount}</td>
                            <td style="padding:10px;border-bottom:1px solid #f3f4f6;">
                                <fmt:formatDate value="${p.expireAtDate}" pattern="yyyy-MM-dd HH:mm"/>
                            </td>
                            <td style="padding:10px;border-bottom:1px solid #f3f4f6;text-align:right;">
                                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/promotions?action=edit&id=${p.id}">Edit</a>
                                <form action="${pageContext.request.contextPath}/admin/promotions" method="post" style="display:inline;" onsubmit="return confirm('Delete this promotion?');">
                                    <input type="hidden" name="action" value="delete"/>
                                    <input type="hidden" name="id" value="${p.id}"/>
                                    <button type="submit" class="btn btn-danger">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</div>

