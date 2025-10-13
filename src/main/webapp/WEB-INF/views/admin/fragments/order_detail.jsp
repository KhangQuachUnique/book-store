<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<style>
    .order-detail-wrapper {
        background: #fff;
        border-radius: 8px;
        padding: 24px;
    }

    .order-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-bottom: 16px;
        border-bottom: 2px solid #e9ecef;
        margin-bottom: 24px;
    }

    .order-id-title {
        font-size: 1.5rem;
        font-weight: 700;
        color: #2c3e50;
        margin: 0;
    }

    .order-date {
        font-size: 0.9rem;
        color: #6c757d;
        margin-top: 4px;
    }

    .status-badge {
        padding: 8px 16px;
        border-radius: 20px;
        font-weight: 600;
        font-size: 0.875rem;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }

    .status-pending_payment { background: #fff3cd; color: #856404; }
    .status-processing { background: #cce5ff; color: #004085; }
    .status-waiting_delivery { background: #d1ecf1; color: #0c5460; }
    .status-delivered { background: #d4edda; color: #155724; }
    .status-canceled { background: #f8d7da; color: #721c24; }

    .info-section {
        margin-bottom: 24px;
    }

    .section-title {
        font-size: 1.1rem;
        font-weight: 600;
        color: #495057;
        padding-bottom: 8px;
        border-bottom: 1px solid #e9ecef;
    }

    .info-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 20px;
        margin-bottom: 30px;
    }

    .info-item {
        display: flex;
        flex-direction: column;
    }

    .info-label {
        font-size: 0.85rem;
        font-weight: 500;
        color: #6c757d;
        letter-spacing: 0.5px;
    }

    .info-value {
        font-size: 1rem;
        color: #212529;
        font-weight: 500;
    }

    .items-table {
        width: 100%;
        border-collapse: separate;
        border-spacing: 0;
        border: 1px solid #e9ecef;
        border-radius: 8px;
        overflow: hidden;
    }

    .items-table thead {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
    }

    .items-table thead th {
        padding: 14px 16px;
        font-weight: 600;
        text-align: left;
        font-size: 0.875rem;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }

    .items-table tbody td {
        padding: 14px 16px;
        border-bottom: 1px solid #e9ecef;
        font-size: 0.95rem;
    }

    .items-table tbody tr:last-child td {
        border-bottom: none;
    }

    .items-table tbody tr:hover {
        background-color: #f8f9fa;
    }

    .book-title {
        font-weight: 600;
        color: #2c3e50;
    }

    .quantity-badge {
        display: inline-block;
        padding: 4px 12px;
        background: #e9ecef;
        border-radius: 12px;
        font-weight: 600;
        font-size: 0.875rem;
        color: #495057;
    }

    .price-cell {
        font-weight: 600;
        color: #495057;
    }

    .total-cell {
        font-weight: 700;
        color: #667eea;
        font-size: 1.05rem;
    }

    .order-summary {
        margin-top: 24px;
        padding: 20px;
        background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        border-radius: 8px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .summary-label {
        font-size: 1.2rem;
        font-weight: 600;
        color: #495057;
    }

    .summary-amount {
        font-size: 1.8rem;
        font-weight: 700;
        color: #667eea;
    }

    .empty-message {
        text-align: center;
        padding: 40px;
        color: #6c757d;
        font-size: 1rem;
    }

    @media (max-width: 768px) {
        .info-grid {
            grid-template-columns: 1fr;
        }

        .order-header {
            flex-direction: column;
            align-items: flex-start;
            gap: 12px;
        }
    }
</style>

<c:choose>
    <c:when test="${empty order}">
        <div class="alert alert-danger">
            <strong>Order not found.</strong>
            <p class="mb-0 mt-2">The requested order could not be found in the system.</p>
        </div>
    </c:when>
    <c:otherwise>
        <div class="order-detail-wrapper">
            <!-- Order Header -->
            <div class="order-header">
                <div>
                    <h2 class="order-id-title">Order #${order.id}</h2>
                    <div class="order-date">
                        <fmt:formatDate value="${order.createdAt}" pattern="EEEE, dd MMMM yyyy 'at' HH:mm"/>
                    </div>
                </div>
                <span class="status-badge status-${fn:toLowerCase(fn:replace(order.status, '_', '-'))}">
                    ${order.status}
                </span>
            </div>

            <!-- Customer & Payment Information -->
            <div class="info-section">
                <h3 class="section-title">Order Information</h3>
                <div class="info-grid">
                    <div class="info-item">
                        <span class="info-label">Customer Name</span>
                        <span class="info-value">${order.user != null ? order.user.name : 'N/A'}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Email Address</span>
                        <span class="info-value">${order.user != null ? order.user.email : 'N/A'}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Payment Method</span>
                        <span class="info-value">${order.paymentMethod}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Order Status</span>
                        <span class="info-value">${order.status}</span>
                    </div>
                </div>
            </div>

            <!-- Order Items -->
            <div class="info-section">
                <h3 class="section-title">Order Items</h3>
                <div class="table-responsive">
                    <table class="items-table">
                        <thead>
                            <tr>
                                <th style="width: 50%">Book Title</th>
                                <th style="width: 15%; text-align: center">Quantity</th>
                                <th style="width: 17.5%; text-align: right">Unit Price</th>
                                <th style="width: 17.5%; text-align: right">Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty order.items}">
                                    <tr>
                                        <td colspan="4" class="empty-message">
                                            No items in this order
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="item" items="${order.items}">
                                        <tr>
                                            <td>
                                                <span class="book-title">
                                                    ${item.book != null ? item.book.title : 'N/A'}
                                                </span>
                                            </td>
                                            <td style="text-align: center">
                                                <span class="quantity-badge">×${item.quantity}</span>
                                            </td>
                                            <td style="text-align: right">
                                                <span class="price-cell"><fmt:formatNumber value="${item.price}" pattern="#,##0"/>₫</span>
                                            </td>
                                            <td style="text-align: right">
                                                <span class="total-cell"><fmt:formatNumber value="${item.price * item.quantity}" pattern="#,##0"/>₫</span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Order Summary -->
            <div class="order-summary">
                <span class="summary-label">Total Amount</span>
                <span class="summary-amount"><fmt:formatNumber value="${order.totalAmount}" pattern="#,##0"/>₫</span>
            </div>
        </div>
    </c:otherwise>
</c:choose>
