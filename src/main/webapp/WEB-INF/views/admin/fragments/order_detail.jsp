<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${not empty order}">
    <div class="order-detail">
        <!-- Order Header -->
        <div class="order-header">
            <h3>Order #${order.id}</h3>
            <div class="order-status">
                <span class="status-badge status-${fn:toLowerCase(order.status)}">${order.status}</span>
            </div>
        </div>

        <!-- Customer Information -->
        <div class="detail-section">
            <h4>Customer Information</h4>
            <div class="info-grid">
                <div class="info-item">
                    <label>Name:</label>
                    <span>${order.user.name}</span>
                </div>
                <div class="info-item">
                    <label>Email:</label>
                    <span>${order.user.email}</span>
                </div>
                <div class="info-item">
                    <label>Phone:</label>
                    <span>${order.user.phone}</span>
                </div>
            </div>
        </div>

        <!-- Order Items -->
        <div class="detail-section">
            <h4>Order Items</h4>
            <div class="order-items">
                <c:forEach var="item" items="${order.orderItems}">
                    <div class="order-item">
                        <div class="item-image">
                            <img src="${pageContext.request.contextPath}/assets/images/${item.book.imageUrl}" 
                                 alt="${item.book.title}" 
                                 style="width: 60px; height: 80px; object-fit: cover; border-radius: 4px;">
                        </div>
                        <div class="item-details">
                            <h5>${item.book.title}</h5>
                            <p class="item-author">by ${item.book.author}</p>
                            <div class="item-meta">
                                <span>Quantity: ${item.quantity}</span>
                                <span>Price: <fmt:formatNumber value="${item.price}" pattern="#,##0"/>₫</span>
                            </div>
                        </div>
                        <div class="item-total">
                            <strong><fmt:formatNumber value="${item.price * item.quantity}" pattern="#,##0"/>₫</strong>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- Order Summary -->
        <div class="detail-section">
            <h4>Order Summary</h4>
            <div class="order-summary">
                <div class="summary-row">
                    <label>Subtotal:</label>
                    <span><fmt:formatNumber value="${order.totalAmount}" pattern="#,##0"/>₫</span>
                </div>
                <div class="summary-row">
                    <label>Payment Method:</label>
                    <span>${order.paymentMethod}</span>
                </div>
                <div class="summary-row">
                    <label>Order Date:</label>
                    <span><fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span>
                </div>
                <c:if test="${not empty order.deliveryAddress}">
                    <div class="summary-row">
                        <label>Delivery Address:</label>
                        <span>${order.deliveryAddress}</span>
                    </div>
                </c:if>
                <div class="summary-row total">
                    <label>Total Amount:</label>
                    <span><strong><fmt:formatNumber value="${order.totalAmount}" pattern="#,##0"/>₫</strong></span>
                </div>
            </div>
        </div>

        <!-- Actions -->
        <div class="detail-actions">
            <button class="btn btn-primary" onclick="updateOrderStatus(${order.id})">
                Update Status
            </button>
            <button class="btn btn-secondary" onclick="closeDetails()">
                Close
            </button>
        </div>
    </div>

    <style>
        .order-detail {
            max-width: 800px;
            margin: 0 auto;
        }

        .order-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 2px solid #e2e8f0;
        }

        .order-header h3 {
            margin: 0;
            color: #1a202c;
        }

        .status-badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 0.75rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }

        .status-pending_payment { background: #fed7d7; color: #c53030; }
        .status-processing { background: #bee3f8; color: #2b6cb0; }
        .status-waiting_delivery { background: #faf089; color: #744210; }
        .status-delivered { background: #c6f6d5; color: #22543d; }
        .status-canceled { background: #fed7d7; color: #c53030; }

        .detail-section {
            margin-bottom: 25px;
        }

        .detail-section h4 {
            margin: 0 0 15px 0;
            color: #2d3748;
            font-size: 1.1rem;
            border-bottom: 1px solid #e2e8f0;
            padding-bottom: 8px;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 12px;
        }

        .info-item {
            display: flex;
            flex-direction: column;
            gap: 4px;
        }

        .info-item label {
            font-weight: 600;
            color: #4a5568;
            font-size: 0.875rem;
        }

        .info-item span {
            color: #2d3748;
        }

        .order-items {
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .order-item {
            display: flex;
            gap: 15px;
            padding: 15px;
            background: #f7fafc;
            border-radius: 8px;
            border: 1px solid #e2e8f0;
        }

        .item-details {
            flex: 1;
        }

        .item-details h5 {
            margin: 0 0 6px 0;
            color: #1a202c;
            font-size: 1rem;
        }

        .item-author {
            margin: 0 0 8px 0;
            color: #718096;
            font-size: 0.875rem;
        }

        .item-meta {
            display: flex;
            gap: 15px;
            font-size: 0.875rem;
            color: #4a5568;
        }

        .item-total {
            display: flex;
            align-items: center;
            font-size: 1.1rem;
            color: #2d3748;
        }

        .order-summary {
            background: #f7fafc;
            padding: 20px;
            border-radius: 8px;
            border: 1px solid #e2e8f0;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 8px 0;
            border-bottom: 1px solid #e2e8f0;
        }

        .summary-row:last-child {
            border-bottom: none;
        }

        .summary-row.total {
            font-size: 1.1rem;
            margin-top: 10px;
            padding-top: 15px;
            border-top: 2px solid #e2e8f0;
        }

        .summary-row label {
            font-weight: 600;
            color: #4a5568;
        }

        .detail-actions {
            display: flex;
            gap: 12px;
            justify-content: center;
            margin-top: 25px;
            padding-top: 20px;
            border-top: 2px solid #e2e8f0;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s ease;
        }

        .btn-primary {
            background: #3182ce;
            color: white;
        }

        .btn-primary:hover {
            background: #2c5282;
        }

        .btn-secondary {
            background: #e2e8f0;
            color: #4a5568;
        }

        .btn-secondary:hover {
            background: #cbd5e0;
        }

        @media (max-width: 768px) {
            .order-header {
                flex-direction: column;
                gap: 10px;
                align-items: flex-start;
            }

            .order-item {
                flex-direction: column;
                text-align: center;
            }

            .item-meta {
                justify-content: center;
            }

            .detail-actions {
                flex-direction: column;
            }

            .info-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</c:if>

<c:if test="${empty order}">
    <div class="alert alert-danger">
        <h4>Order Not Found</h4>
        <p>The requested order could not be found or may have been deleted.</p>
    </div>
</c:if>