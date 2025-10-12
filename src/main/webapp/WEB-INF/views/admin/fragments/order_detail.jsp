<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
    <c:when test="${empty order}">
        <div class="alert alert-danger">Order not found.</div>
    </c:when>
    <c:otherwise>
        <div class="row mb-3">
            <div class="col-md-6">
                <h6>Customer Information</h6>
                <p class="mb-1"><strong>Name:</strong> ${order.user != null ? order.user.name : 'N/A'}</p>
                <p class="mb-1"><strong>Email:</strong> ${order.user != null ? order.user.email : 'N/A'}</p>
            </div>
            <div class="col-md-6">
                <h6>Order Information</h6>
                <p class="mb-1"><strong>Order ID:</strong> #${order.id}</p>
                <p class="mb-1"><strong>Date:</strong> <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/></p>
                <p class="mb-1"><strong>Payment:</strong> ${order.paymentMethod}</p>
                <p class="mb-1"><strong>Status:</strong> <span class="badge bg-info">${order.status}</span></p>
            </div>
        </div>
        <hr>
        <h6>Order Items</h6>
        <div class="table-responsive">
            <table class="table table-sm">
                <thead>
                    <tr>
                        <th>Book</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty order.items}">
                            <tr><td colspan="4" class="text-center text-muted">No items</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="item" items="${order.items}">
                                <tr>
                                    <td>${item.book != null ? item.book.title : 'N/A'}</td>
                                    <td>${item.quantity}</td>
                                    <td>$<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/></td>
                                    <td>$<fmt:formatNumber value="${item.price * item.quantity}" pattern="#,##0.00"/></td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
        <div class="text-end">
            <h5>Total Amount: <span class="text-primary">$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span></h5>
        </div>
    </c:otherwise>
</c:choose>
