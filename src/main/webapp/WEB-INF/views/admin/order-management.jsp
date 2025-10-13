<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/orderManagement.css">

<div class="container">
    <h1 class="page-title">Order Management</h1>
    <p class="text-muted" style="margin-bottom: 1rem;">Manage all customer orders, update status, and view details.</p>

    <!-- Flash messages -->
    <c:if test="${not empty sessionScope.flash_success}">
        <div class="alert alert-success">${sessionScope.flash_success}</div>
        <c:remove var="flash_success" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.flash_error}">
        <div class="alert alert-danger">${sessionScope.flash_error}</div>
        <c:remove var="flash_error" scope="session"/>
    </c:if>

    <!-- Search and Filter -->
    <div class="card search-card">
        <div class="card-header">
            <h2 class="card-title">Search Orders</h2>
        </div>
        <div class="card-content">
            <form method="get" action="${pageContext.request.contextPath}/admin/orders" id="searchForm" class="search-form">
                <div class="grid" style="display:grid; grid-template-columns: 1fr 220px 220px; gap: 12px; align-items: end;">
                    <div>
                        <label for="keyword" class="label">Keyword</label>
                        <input type="text" class="input" id="keyword" name="keyword"
                               placeholder="Search by Order ID, Customer Name, or Email..."
                               value="${keyword}">
                    </div>
                    <div>
                        <label for="status" class="label">Status</label>
                        <select class="input" id="status" name="status" onchange="this.form.submit()">
                            <option value="" ${empty filterStatus ? 'selected' : ''}>All</option>
                            <option value="PENDING_PAYMENT" ${filterStatus == 'PENDING_PAYMENT' ? 'selected' : ''}>Pending Payment</option>
                            <option value="PROCESSING" ${filterStatus == 'PROCESSING' ? 'selected' : ''}>Processing</option>
                            <option value="WAITING_DELIVERY" ${filterStatus == 'WAITING_DELIVERY' ? 'selected' : ''}>Waiting for Delivery</option>
                            <option value="DELIVERED" ${filterStatus == 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                             <option value="CANCELED" ${filterStatus == 'CANCELED' ? 'selected' : ''}>Cancelled</option>
                        </select>
                    </div>
                    <div>
                        <button type="submit" class="btn btn-primary" style="width:100%">Search</button>
                        <button type="button" class="btn btn-secondary" style="width:100%; margin-top:8px" onclick="clearSearch()">Clear</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Orders Table -->
    <div class="card">
        <div class="card-header">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <h2 class="card-title">Orders List</h2>
                <span class="text-muted" style="font-size: 0.9rem;">
                    Showing ${empty orders ? 0 : (currentPage - 1) * 10 + 1} - ${(currentPage - 1) * 10 + fn:length(orders)} of ${totalOrders} orders
                </span>
            </div>
        </div>
        <div class="card-content">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <c:choose>
                <c:when test="${empty orders}">
                    <div class="alert alert-info">No orders found.</div>
                </c:when>
                <c:otherwise>
                    <table class="table">
                        <thead>
                        <tr class="table-header">
                            <th>Order ID</th>
                            <th>Customer</th>
                            <th>Email</th>
                            <th>Date</th>
                            <th>Total</th>
                            <th>Payment</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="order" items="${orders}">
                            <tr id="order-row-${order.id}">
                                <td><strong>#${order.id}</strong></td>
                                <td>${order.user.name}</td>
                                <td>${order.user.email}</td>
                                <td><fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td><strong><fmt:formatNumber value="${order.totalAmount}" pattern="#,##0"/>₫</strong></td>
                                <td><span class="badge">${order.paymentMethod}</span></td>
                                <td>
                                    <select class="input status-select ${fn:toLowerCase(order.status)}" data-order-id="${order.id}" onchange="updateStatusQuickFromSelect(this)">
                                        <option value="PENDING_PAYMENT" ${order.status == 'PENDING_PAYMENT' ? 'selected' : ''}>Pending Payment</option>
                                        <option value="PROCESSING" ${order.status == 'PROCESSING' ? 'selected' : ''}>Processing</option>
                                        <option value="WAITING_DELIVERY" ${order.status == 'WAITING_DELIVERY' ? 'selected' : ''}>Waiting for Delivery</option>
                                        <option value="DELIVERED" ${order.status == 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                        <option value="CANCELED" ${order.status == 'CANCELED' ? 'selected' : ''}>Cancelled</option>
                                    </select>
                                </td>
                                <td class="action-buttons">
                                        <button class="btn btn-outline btn-sm" data-order-id="${order.id}" onclick="viewOrderDetails(this.getAttribute('data-order-id'))">View</button>
                                        <button class="btn btn-destructive btn-sm" data-order-id="${order.id}" onclick="confirmDelete(this.getAttribute('data-order-id'))">Delete</button>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                    <!-- Pagination -->
                    <c:if test="${totalPages > 1}">
                        <div class="pagination-wrapper">
                            <div class="pagination">
                                <!-- Previous Button -->
                                <c:choose>
                                    <c:when test="${currentPage > 1}">
                                        <a href="?page=${currentPage - 1}&keyword=${keyword}&status=${filterStatus}" class="pagination-btn">
                                            « Previous
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="pagination-btn disabled">« Previous</span>
                                    </c:otherwise>
                                </c:choose>

                                <!-- Page Numbers -->
                                <c:choose>
                                    <c:when test="${totalPages <= 7}">
                                        <!-- Show all pages if total is 7 or less -->
                                        <c:forEach var="i" begin="1" end="${totalPages}">
                                            <c:choose>
                                                <c:when test="${i == currentPage}">
                                                    <span class="pagination-btn active">${i}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="?page=${i}&keyword=${keyword}&status=${filterStatus}" class="pagination-btn">${i}</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- Show first page -->
                                        <c:if test="${currentPage > 3}">
                                            <a href="?page=1&keyword=${keyword}&status=${filterStatus}" class="pagination-btn">1</a>
                                            <c:if test="${currentPage > 4}">
                                                <span class="pagination-ellipsis">...</span>
                                            </c:if>
                                        </c:if>

                                        <!-- Show pages around current page -->
                                        <c:forEach var="i" begin="${currentPage - 2}" end="${currentPage + 2}">
                                            <c:if test="${i >= 1 && i <= totalPages}">
                                                <c:choose>
                                                    <c:when test="${i == currentPage}">
                                                        <span class="pagination-btn active">${i}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="?page=${i}&keyword=${keyword}&status=${filterStatus}" class="pagination-btn">${i}</a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </c:forEach>

                                        <!-- Show last page -->
                                        <c:if test="${currentPage < totalPages - 2}">
                                            <c:if test="${currentPage < totalPages - 3}">
                                                <span class="pagination-ellipsis">...</span>
                                            </c:if>
                                            <a href="?page=${totalPages}&keyword=${keyword}&status=${filterStatus}" class="pagination-btn">${totalPages}</a>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>

                                <!-- Next Button -->
                                <c:choose>
                                    <c:when test="${currentPage < totalPages}">
                                        <a href="?page=${currentPage + 1}&keyword=${keyword}&status=${filterStatus}" class="pagination-btn">
                                            Next »
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="pagination-btn disabled">Next »</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>

            <!-- Inline Details Panel -->
            <div id="detailsPanel" class="card" style="display:none; margin-top:12px;">
                <div class="card-header">
                    <div style="display:flex; justify-content:space-between; align-items:center;">
                        <h3 class="card-title" style="margin:0">Order Details</h3>
                        <button class="btn btn-secondary btn-sm" onclick="closeDetails()">Close</button>
                    </div>
                </div>
                <div class="card-content" id="orderDetailsBody">
                    <div class="text-muted">Loading...</div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    const ctx = '${pageContext.request.contextPath}';
    let deleteOrderIdTemp = null;

    function postForm(action, params) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = ctx + '/admin/orders';
        const actionInput = document.createElement('input');
        actionInput.type = 'hidden';
        actionInput.name = 'action';
        actionInput.value = action;
        form.appendChild(actionInput);
        Object.entries(params || {}).forEach(([key, value]) => {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = key;
            input.value = value;
            form.appendChild(input);
        });
        document.body.appendChild(form);
        form.submit();
    }

    function updateStatusQuick(orderId, newStatus) {
        if (!confirm('Update order #' + orderId + ' status to ' + newStatus + '?')) {
            location.reload();
            return;
        }
        postForm('updateStatus', { orderId: orderId, status: newStatus });
    }

    function updateStatusQuickFromSelect(selectEl) {
        const orderId = selectEl.getAttribute('data-order-id');
        const newStatus = selectEl.value;
        updateStatusQuick(orderId, newStatus);
    }

    function viewOrderDetails(orderId) {
        const panel = document.getElementById('detailsPanel');
        const body = document.getElementById('orderDetailsBody');
        panel.style.display = 'block';
        body.innerHTML = '<div class="text-muted">Loading...</div>';

        fetch(ctx + '/admin/orders/' + orderId, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
            .then(r => r.text())
            .then(html => { body.innerHTML = html; })
            .catch(() => { body.innerHTML = '<div class="alert alert-danger">Failed to load order details</div>'; });
    }

    function closeDetails() {
        const panel = document.getElementById('detailsPanel');
        const body = document.getElementById('orderDetailsBody');
        body.innerHTML = '';
        panel.style.display = 'none';
    }

    function confirmDelete(orderId) {
        if (confirm('Are you sure you want to delete order #' + orderId + '? This action cannot be undone.')) {
            postForm('delete', { orderId: orderId });
        }
    }

    function clearSearch() {
        window.location.href = ctx + '/admin/orders';
    }
</script>