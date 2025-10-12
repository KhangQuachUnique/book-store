<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        :root {
            --red-primary: #dc3545;
            --red-dark: #c82333;
            --red-light: #e74c3c;
            --red-lighter: #ff6b6b;
            --red-gradient-1: #d63031;
            --red-gradient-2: #e17055;
        }

        body {
            background: linear-gradient(135deg, #fff5f5 0%, #ffe5e5 100%);
            min-height: 100vh;
        }

        h2 {
            color: var(--red-dark);
            font-weight: bold;
        }

        .stats-card {
            transition: all 0.3s;
            border: none;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(220, 53, 69, 0.2);
        }
        
        .stats-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 8px 20px rgba(220, 53, 69, 0.3);
        }

        .stats-card.bg-red-1 {
            background: linear-gradient(135deg, var(--red-primary) 0%, var(--red-dark) 100%);
        }

        .stats-card.bg-red-2 {
            background: linear-gradient(135deg, var(--red-light) 0%, var(--red-primary) 100%);
        }

        .stats-card.bg-red-3 {
            background: linear-gradient(135deg, var(--red-lighter) 0%, var(--red-light) 100%);
        }

        .stats-card.bg-red-4 {
            background: linear-gradient(135deg, var(--red-gradient-1) 0%, var(--red-gradient-2) 100%);
        }

        .search-container {
            margin-bottom: 20px;
        }

        .btn-primary {
            background: linear-gradient(135deg, var(--red-primary) 0%, var(--red-dark) 100%);
            border: none;
            transition: all 0.3s;
        }

        .btn-primary:hover {
            background: linear-gradient(135deg, var(--red-dark) 0%, #a71d2a 100%);
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(220, 53, 69, 0.4);
        }

        .btn-secondary {
            background-color: #6c757d;
            border: none;
        }

        .btn-secondary:hover {
            background-color: #5a6268;
        }

        .card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .card-header {
            background: linear-gradient(135deg, var(--red-primary) 0%, var(--red-dark) 100%) !important;
            color: white !important;
            border-radius: 10px 10px 0 0 !important;
            border: none;
        }

        .table-dark {
            background: linear-gradient(135deg, var(--red-primary) 0%, var(--red-dark) 100%);
        }

        .table-striped tbody tr:nth-of-type(odd) {
            background-color: rgba(220, 53, 69, 0.05);
        }

        .table-hover tbody tr:hover {
            background-color: rgba(220, 53, 69, 0.1);
        }

        .badge.bg-warning {
            background-color: #ff9f43 !important;
        }

        .badge.bg-info {
            background-color: #54a0ff !important;
        }

        .badge.bg-primary {
            background: linear-gradient(135deg, var(--red-primary) 0%, var(--red-light) 100%) !important;
        }

        .badge.bg-success {
            background-color: #00b894 !important;
        }

        .badge.bg-danger {
            background-color: #d63031 !important;
        }

        .btn-sm.btn-primary {
            background: var(--red-primary);
            border: none;
        }

        .btn-sm.btn-primary:hover {
            background: var(--red-dark);
        }

        .btn-sm.btn-info {
            background-color: var(--red-light);
            border: none;
        }

        .btn-sm.btn-info:hover {
            background-color: var(--red-primary);
        }

        .modal-header.bg-primary {
            background: linear-gradient(135deg, var(--red-primary) 0%, var(--red-dark) 100%) !important;
        }

        .modal-header.bg-info {
            background: linear-gradient(135deg, var(--red-light) 0%, var(--red-primary) 100%) !important;
        }

        .form-select:focus,
        .form-control:focus {
            border-color: var(--red-primary);
            box-shadow: 0 0 0 0.25rem rgba(220, 53, 69, 0.25);
        }

        .alert-info {
            background-color: #ffe5e5;
            border-color: #ffcccc;
            color: var(--red-dark);
        }

        .text-success {
            color: var(--red-primary) !important;
        }
    </style>
</head>
<body>
    <div class="container-fluid py-4">
        <h2 class="mb-4"><i class="bi bi-receipt"></i> Order Management</h2>
        
        <!-- Statistics Cards -->
        <div class="row mb-4">
            <div class="col-md-3">
                <div class="card stats-card bg-red-1 text-white">
                    <div class="card-body">
                        <h5 class="card-title"><i class="bi bi-cash-stack"></i> Daily Revenue</h5>
                        <h3 class="card-text" id="dailyRevenue">$0</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stats-card bg-success text-white">
                    <div class="card-body">
                        <h5 class="card-title">Daily Orders</h5>
                        <h3 class="card-text" id="dailyOrders">0</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stats-card bg-info text-white">
                    <div class="card-body">
                        <h5 class="card-title">Monthly Revenue</h5>
                        <h3 class="card-text" id="monthlyRevenue">$0</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stats-card bg-warning text-white">
                    <div class="card-body">
                        <h5 class="card-title">Monthly Orders</h5>
                        <h3 class="card-text" id="monthlyOrders">0</h3>
                    </div>
                </div>
            </div>
        </div>

        <!-- Top Selling Books -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Top Selling Books</h5>
                    </div>
                    <div class="card-body">
                        <div id="topBooksChart"></div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Search and Filter -->
        <div class="row mb-3">
            <div class="col-md-8">
                <div class="search-container">
                    <div class="input-group">
                        <input type="text" id="searchOrder" class="form-control" placeholder="Search by Order ID, Customer Name, or Status...">
                        <button class="btn btn-primary" type="button" onclick="searchOrders()">
                            <i class="bi bi-search"></i> Search
                        </button>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <select class="form-select" id="statusFilter" onchange="filterOrders()">
                    <option value="">All Statuses</option>
                    <option value="pending">Pending</option>
                    <option value="paid">Paid</option>
                    <option value="shipped">Shipped</option>
                    <option value="completed">Completed</option>
                    <option value="cancelled">Cancelled</option>
                </select>
            </div>
        </div>

        <!-- Orders Table -->
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Customer</th>
                        <th>Date</th>
                        <th>Total</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="ordersTableBody">
                    <!-- Orders will be populated here -->
                </tbody>
            </table>
        </div>
    </div>

    <!-- Status Update Modal -->
    <div class="modal fade" id="updateStatusModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Update Order Status</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="updateOrderId">
                    <select class="form-select" id="updateStatus">
                        <option value="pending">Pending</option>
                        <option value="paid">Paid</option>
                        <option value="shipped">Shipped</option>
                        <option value="completed">Completed</option>
                        <option value="cancelled">Cancelled</option>
                    </select>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary" onclick="updateOrderStatus()">Update</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script>
        // Load initial data
        document.addEventListener('DOMContentLoaded', function() {
            loadOrders();
            loadDailyStats();
            loadMonthlyStats();
            loadTopBooks();
        });

        // Load all orders
        function loadOrders() {
            fetch('/api/admin/orders/')
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        displayOrders(data.data);
                    } else {
                        alert('Error loading orders: ' + data.message);
                    }
                })
                .catch(error => console.error('Error:', error));
        }

        // Display orders in table
        function displayOrders(orders) {
            const tableBody = document.getElementById('ordersTableBody');
            tableBody.innerHTML = '';

            orders.forEach(order => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${order.id}</td>
                    <td>${order.user ? order.user.name : 'N/A'}</td>
                    <td>${new Date(order.orderDate).toLocaleDateString()}</td>
                    <td>$${order.totalAmount.toFixed(2)}</td>
                    <td>
                        <span class="badge bg-${getStatusBadgeClass(order.status)}">
                            ${order.status}
                        </span>
                    </td>
                    <td>
                        <button class="btn btn-sm btn-primary" onclick="showUpdateStatusModal(${order.id}, '${order.status}')">
                            <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-sm btn-info" onclick="viewOrderDetails(${order.id})">
                            <i class="bi bi-eye"></i>
                        </button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        }

        // Load statistics
        function loadDailyStats() {
            fetch('/api/admin/orders/stats?period=daily')
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        document.getElementById('dailyRevenue').textContent = '$' + data.data.revenue.toFixed(2);
                        document.getElementById('dailyOrders').textContent = data.data.orderCount;
                    }
                })
                .catch(error => console.error('Error:', error));
        }

        function loadMonthlyStats() {
            fetch('/api/admin/orders/stats?period=monthly')
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        document.getElementById('monthlyRevenue').textContent = '$' + data.data.revenue.toFixed(2);
                        document.getElementById('monthlyOrders').textContent = data.data.orderCount;
                    }
                })
                .catch(error => console.error('Error:', error));
        }

        // Load top selling books
        function loadTopBooks() {
            fetch('/api/admin/orders/top-books?limit=5')
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        displayTopBooks(data.data.books);
                    }
                })
                .catch(error => console.error('Error:', error));
        }

        // Display top books chart
        function displayTopBooks(books) {
            const ctx = document.createElement('canvas');
            document.getElementById('topBooksChart').appendChild(ctx);

            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: books.map(book => book.title),
                    datasets: [{
                        label: 'Sales Count',
                        data: books.map(book => book.salesCount),
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        }

        // Show update status modal
        function showUpdateStatusModal(orderId, currentStatus) {
            document.getElementById('updateOrderId').value = orderId;
            document.getElementById('updateStatus').value = currentStatus;
            new bootstrap.Modal(document.getElementById('updateStatusModal')).show();
        }

        // Update order status
        function updateOrderStatus() {
            const orderId = document.getElementById('updateOrderId').value;
            const status = document.getElementById('updateStatus').value;

            fetch(`/api/admin/orders/status/${orderId}?status=${status}`, {
                method: 'PUT'
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    bootstrap.Modal.getInstance(document.getElementById('updateStatusModal')).hide();
                    loadOrders(); // Reload orders to show updated status
                } else {
                    alert('Error updating status: ' + data.message);
                }
            })
            .catch(error => console.error('Error:', error));
        }

        // Search orders
        function searchOrders() {
            const searchTerm = document.getElementById('searchOrder').value.toLowerCase();
            const rows = document.getElementById('ordersTableBody').getElementsByTagName('tr');

            Array.from(rows).forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(searchTerm) ? '' : 'none';
            });
        }

        // Filter orders by status
        function filterOrders() {
            const status = document.getElementById('statusFilter').value.toLowerCase();
            const rows = document.getElementById('ordersTableBody').getElementsByTagName('tr');

            Array.from(rows).forEach(row => {
                if (!status) {
                    row.style.display = '';
                    return;
                }
                const statusCell = row.querySelector('td:nth-child(5)').textContent.toLowerCase();
                row.style.display = statusCell.includes(status) ? '' : 'none';
            });
        }

        // Helper function to get badge class based on status
        function getStatusBadgeClass(status) {
            const classes = {
                'pending': 'warning',
                'paid': 'info',
                'shipped': 'primary',
                'completed': 'success',
                'cancelled': 'danger'
            };
            return classes[status] || 'secondary';
        }

        // View order details
        function viewOrderDetails(orderId) {
            // Implement order details view
            console.log('View details for order:', orderId);
        }
    </script>
</body>
</html>