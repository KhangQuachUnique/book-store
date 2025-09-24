<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Management - BookieCake</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Table -->
    <link rel="stylesheet" href="https://unpkg.com/bootstrap-table@1.22.1/dist/bootstrap-table.min.css">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <!-- Custom CSS -->
    <style>
        .card {
            box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
            margin-bottom: 1rem;
        }
        .status-badge {
            font-size: 0.875rem;
            padding: 0.25rem 0.5rem;
            border-radius: 0.25rem;
        }
        .status-pending { background-color: #ffc107; color: #000; }
        .status-paid { background-color: #0dcaf0; color: #fff; }
        .status-shipped { background-color: #0d6efd; color: #fff; }
        .status-completed { background-color: #198754; color: #fff; }
        .status-cancelled { background-color: #dc3545; color: #fff; }
    </style>
</head>
<body>
    <%@ include file="admin-header.jsp" %>
    
    <div class="container-fluid py-4">
        <h2 class="mb-4">Order Management</h2>
        
        <!-- Stats Cards -->
        <div class="row mb-4">
            <div class="col-md-3">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Today's Orders</h5>
                        <h3 id="todayOrders" class="card-text">Loading...</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Today's Revenue</h5>
                        <h3 id="todayRevenue" class="card-text">Loading...</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Total Orders</h5>
                        <h3 id="totalOrders" class="card-text">Loading...</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Total Revenue</h5>
                        <h3 id="totalRevenue" class="card-text">Loading...</h3>
                    </div>
                </div>
            </div>
        </div>

        <!-- Charts Row -->
        <div class="row mb-4">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Daily Revenue</h5>
                        <canvas id="revenueChart"></canvas>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Top Selling Books</h5>
                        <canvas id="topBooksChart"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <!-- Orders Table -->
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Recent Orders</h5>
                <table id="ordersTable" class="table">
                    <thead>
                        <tr>
                            <th data-field="id">Order ID</th>
                            <th data-field="userName">Customer</th>
                            <th data-field="createdAt">Order Date</th>
                            <th data-field="totalAmount" data-formatter="formatCurrency">Amount</th>
                            <th data-field="paymentMethod">Payment</th>
                            <th data-field="statusName" data-formatter="formatStatus">Status</th>
                            <th data-field="actions" data-formatter="formatActions">Actions</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>

    <!-- Status Update Modal -->
    <div class="modal fade" id="statusModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Update Order Status</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <select class="form-select" id="statusSelect">
                        <option value="pending">Pending</option>
                        <option value="paid">Paid</option>
                        <option value="shipped">Shipped</option>
                        <option value="completed">Completed</option>
                        <option value="cancelled">Cancelled</option>
                    </select>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="updateStatus">Update</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- Bootstrap Table -->
    <script src="https://unpkg.com/bootstrap-table@1.22.1/dist/bootstrap-table.min.js"></script>

    <script>
        // Global variables
        let revenueChart = null;
        let topBooksChart = null;
        let currentOrderId = null;

        // Initialize page
        $(document).ready(function() {
            initializeTable();
            loadDailyStats();
            loadTopBooks();
            setupEventHandlers();
        });

        // Format currency
        function formatCurrency(value) {
            return new Intl.NumberFormat('vi-VN', {
                style: 'currency',
                currency: 'VND'
            }).format(value);
        }

        // Format status badge
        function formatStatus(value) {
            return `<span class="status-badge status-${value.toLowerCase()}">${value}</span>`;
        }

        // Format actions column
        function formatActions(value, row) {
            return `
                <button class="btn btn-sm btn-primary" onclick="showStatusModal(${row.id})">
                    Update Status
                </button>
                <button class="btn btn-sm btn-info" onclick="viewOrderDetails(${row.id})">
                    View Details
                </button>
            `;
        }

        // Initialize Bootstrap Table
        function initializeTable() {
            $('#ordersTable').bootstrapTable({
                url: '/BookieCake/api/admin/orders',
                pagination: true,
                search: true,
                sortable: true,
                pageSize: 10,
                pageList: [10, 25, 50, 100]
            });
        }

        // Load daily statistics
        function loadDailyStats() {
            $.get('/BookieCake/api/admin/orders/stats?period=daily', function(response) {
                if (response.success) {
                    const stats = response.data;
                    updateStatsCards(stats);
                    updateRevenueChart(stats);
                }
            });
        }

        // Load top selling books
        function loadTopBooks() {
            $.get('/BookieCake/api/admin/orders/top-books?limit=5', function(response) {
                if (response.success) {
                    updateTopBooksChart(response.data.books);
                }
            });
        }

        // Update statistics cards
        function updateStatsCards(stats) {
            if (stats && stats.length > 0) {
                const today = stats[0] || { orderCount: 0, revenue: 0 };
                const total = stats.reduce((acc, curr) => ({
                    orderCount: acc.orderCount + curr.orderCount,
                    revenue: acc.revenue + curr.revenue
                }), { orderCount: 0, revenue: 0 });

                $('#todayOrders').text(today.orderCount);
                $('#todayRevenue').text(formatCurrency(today.revenue));
                $('#totalOrders').text(total.orderCount);
                $('#totalRevenue').text(formatCurrency(total.revenue));
            }
        }

        // Update revenue chart
        function updateRevenueChart(stats) {
            if (revenueChart) {
                revenueChart.destroy();
            }

            if (stats && stats.length > 0) {
                const ctx = document.getElementById('revenueChart').getContext('2d');
                revenueChart = new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: stats.map(day => day.date).reverse(),
                        datasets: [{
                            label: 'Daily Revenue',
                            data: stats.map(day => day.revenue).reverse(),
                            borderColor: 'rgb(75, 192, 192)',
                            tension: 0.1
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            title: {
                                display: true,
                                text: 'Daily Revenue (Last 30 Days)'
                            }
                        }
                    }
                });
            }
        }

        // Update top books chart
        function updateTopBooksChart(books) {
            if (topBooksChart) {
                topBooksChart.destroy();
            }

            if (books && books.length > 0) {
                const ctx = document.getElementById('topBooksChart').getContext('2d');
                topBooksChart = new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: books.map(book => book.title),
                        datasets: [{
                            label: 'Units Sold',
                            data: books.map(book => book.totalSold),
                            backgroundColor: 'rgba(54, 162, 235, 0.5)'
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            title: {
                                display: true,
                                text: 'Top Selling Books'
                            }
                        }
                    }
                });
            }
        }

        // Show status update modal
        function showStatusModal(orderId) {
            currentOrderId = orderId;
            $('#statusModal').modal('show');
        }

        // View order details
        function viewOrderDetails(orderId) {
            // TODO: Implement order details view
            alert('Order details feature coming soon!');
        }

        // Setup event handlers
        function setupEventHandlers() {
            // Handle status update
            $('#updateStatus').click(function() {
                const status = $('#statusSelect').val();
                
                $.ajax({
                    url: `/BookieCake/api/admin/orders/status/${currentOrderId}`,
                    method: 'PUT',
                    data: { status: status },
                    success: function(response) {
                        if (response.success) {
                            $('#statusModal').modal('hide');
                            $('#ordersTable').bootstrapTable('refresh');
                            loadDailyStats(); // Refresh stats after status update
                        } else {
                            alert('Failed to update status: ' + response.message);
                        }
                    },
                    error: function() {
                        alert('Error updating status');
                    }
                });
            });

            // Refresh data periodically
            setInterval(function() {
                $('#ordersTable').bootstrapTable('refresh');
                loadDailyStats();
                loadTopBooks();
            }, 300000); // Refresh every 5 minutes
        }
    </script>
</body>
</html>