<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="assets/styles/order-management.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="fragment/header.jsp"/>
    
    <div class="container mt-4">
        <h2>Order Management</h2>
        
        <!-- Filters -->
        <div class="row mb-4">
            <div class="col-md-3">
                <select class="form-select" id="statusFilter">
                    <option value="">All Statuses</option>
                    <option value="pending">Pending</option>
                    <option value="paid">Paid</option>
                    <option value="shipped">Shipped</option>
                    <option value="completed">Completed</option>
                    <option value="cancelled">Cancelled</option>
                </select>
            </div>
            <div class="col-md-6">
                <input type="text" class="form-control" id="searchInput" placeholder="Search by customer name, email or phone...">
            </div>
            <div class="col-md-3">
                <button class="btn btn-primary" id="refreshBtn">
                    <i class="fas fa-sync"></i> Refresh
                </button>
            </div>
        </div>
        
        <!-- Orders Table -->
        <div class="table-responsive">
            <table class="table table-striped" id="ordersTable">
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Customer</th>
                        <th>Total Amount</th>
                        <th>Status</th>
                        <th>Payment Method</th>
                        <th>Created At</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Populated by JavaScript -->
                </tbody>
            </table>
        </div>
    </div>

    <!-- Order Detail Modal -->
    <div class="modal fade" id="orderDetailModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Order Detail</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="order-info mb-4">
                        <h6>Order Information</h6>
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>Order ID:</strong> <span id="modalOrderId"></span></p>
                                <p><strong>Customer:</strong> <span id="modalCustomerName"></span></p>
                                <p><strong>Email:</strong> <span id="modalCustomerEmail"></span></p>
                                <p><strong>Phone:</strong> <span id="modalCustomerPhone"></span></p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Status:</strong> <span id="modalOrderStatus"></span></p>
                                <p><strong>Payment Method:</strong> <span id="modalPaymentMethod"></span></p>
                                <p><strong>Created At:</strong> <span id="modalCreatedAt"></span></p>
                                <p><strong>Total Amount:</strong> <span id="modalTotalAmount"></span></p>
                            </div>
                        </div>
                    </div>
                    <div class="order-items">
                        <h6>Order Items</h6>
                        <div class="table-responsive">
                            <table class="table table-sm">
                                <thead>
                                    <tr>
                                        <th>Book</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Subtotal</th>
                                    </tr>
                                </thead>
                                <tbody id="modalOrderItems">
                                    <!-- Populated by JavaScript -->
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Update Status Modal -->
    <div class="modal fade" id="updateStatusModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Update Order Status</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="updateStatusForm">
                        <input type="hidden" id="updateOrderId">
                        <div class="mb-3">
                            <label for="newStatus" class="form-label">New Status</label>
                            <select class="form-select" id="newStatus" required>
                                <option value="pending">Pending</option>
                                <option value="paid">Paid</option>
                                <option value="shipped">Shipped</option>
                                <option value="completed">Completed</option>
                                <option value="cancelled">Cancelled</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary" id="updateStatusBtn">Update Status</button>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="fragment/footer.jsp"/>
    
    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://kit.fontawesome.com/your-kit-code.js"></script>
    <script src="assets/js/pages/order.js"></script>
</body>
</html>