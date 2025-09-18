<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <div class="container-fluid">
                <!-- Header -->
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">
                        <i class="fas fa-shopping-cart"></i> Quản lý đơn hàng
                    </h1>
                    <div>
                        <button type="button" class="btn btn-outline-primary" onclick="refreshTable()">
                            <i class="fas fa-sync-alt"></i> Làm mới
                        </button>
                    </div>
                </div>

                <!-- Filters -->
                <div class="card shadow mb-4">
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-3">
                                <label for="statusFilter" class="form-label">Lọc theo trạng thái:</label>
                                <select id="statusFilter" class="form-select" onchange="filterByStatus()">
                                    <option value="">Tất cả trạng thái</option>
                                    <option value="PENDING">Chờ xử lý</option>
                                    <option value="CONFIRMED">Đã xác nhận</option>
                                    <option value="DELIVERY">Đang giao hàng</option>
                                    <option value="CANCELLED">Đã hủy</option>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <label for="searchInput" class="form-label">Tìm kiếm:</label>
                                <input type="text" id="searchInput" class="form-control"
                                    placeholder="Tìm theo tên khách hàng, email...">
                            </div>
                            <div class="col-md-6 d-flex align-items-end">
                                <button type="button" class="btn btn-primary me-2" onclick="searchOrders()">
                                    <i class="fas fa-search"></i> Tìm kiếm
                                </button>
                                <button type="button" class="btn btn-secondary" onclick="clearFilters()">
                                    <i class="fas fa-times"></i> Xóa bộ lọc
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Orders Table -->
                <div class="card shadow">
                    <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                        <h6 class="m-0 font-weight-bold text-primary">Danh sách đơn hàng</h6>
                        <span id="totalOrders" class="badge badge-info">0 đơn hàng</span>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table id="ordersTable" class="table table-bordered table-hover">
                                <thead class="thead-light">
                                    <tr>
                                        <th data-field="id" data-sortable="true">ID</th>
                                        <th data-field="customerName" data-sortable="true">Khách hàng</th>
                                        <th data-field="customerPhone" data-sortable="true">Số điện thoại</th>
                                        <th data-field="totalAmount" data-sortable="true"
                                            data-formatter="priceFormatter">Tổng tiền</th>
                                        <th data-field="status" data-sortable="true" data-formatter="statusFormatter">
                                            Trạng thái</th>
                                        <th data-field="createdAt" data-sortable="true" data-formatter="dateFormatter">
                                            Ngày đặt</th>
                                        <th data-field="actions" data-formatter="actionFormatter">Hành động</th>
                                    </tr>
                                </thead>
                                <tbody id="ordersTableBody">
                                    <!-- Data will be loaded via AJAX -->
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Order Details Modal -->
            <div class="modal fade" id="orderDetailsModal" tabindex="-1" aria-labelledby="orderDetailsModalLabel"
                aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="orderDetailsModalLabel">Chi tiết đơn hàng</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body" id="orderDetailsContent">
                            <!-- Order details will be loaded here -->
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Update Status Modal -->
            <div class="modal fade" id="updateStatusModal" tabindex="-1" aria-labelledby="updateStatusModalLabel"
                aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="updateStatusModalLabel">Cập nhật trạng thái đơn hàng</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form id="updateStatusForm">
                                <input type="hidden" id="updateOrderId" name="orderId">
                                <div class="mb-3">
                                    <label for="newStatus" class="form-label">Trạng thái mới:</label>
                                    <select id="newStatus" name="status" class="form-select" required>
                                        <option value="">Chọn trạng thái</option>
                                        <option value="CONFIRMED">Xác nhận</option>
                                        <option value="DELIVERY">Giao hàng</option>
                                        <option value="CANCELLED">Hủy đơn</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="statusNotes" class="form-label">Ghi chú (tùy chọn):</label>
                                    <textarea id="statusNotes" name="notes" class="form-control" rows="3"
                                        placeholder="Nhập ghi chú cho việc thay đổi trạng thái..."></textarea>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <button type="button" class="btn btn-primary" onclick="updateOrderStatus()">Cập
                                nhật</button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Required CSS and JS -->
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

            <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

            <script>
                let ordersData = [];

                $(document).ready(function () {
                    loadOrders();
                });

                // Load orders from API
                function loadOrders(status = '') {
                    const url = status ? `/api/orders?status=${status}` : '/api/orders';

                    $.ajax({
                        url: url,
                        method: 'GET',
                        success: function (response) {
                            if (response.success) {
                                ordersData = response.data;
                                populateTable(ordersData);
                                updateTotalCount(ordersData.length);
                            } else {
                                showAlert('error', 'Lỗi tải dữ liệu: ' + response.message);
                            }
                        },
                        error: function () {
                            showAlert('error', 'Không thể tải danh sách đơn hàng');
                        }
                    });
                }

                // Populate table with data
                function populateTable(data) {
                    const tbody = $('#ordersTableBody');
                    tbody.empty();

                    if (data.length === 0) {
                        tbody.append('<tr><td colspan="7" class="text-center">Không có đơn hàng nào</td></tr>');
                        return;
                    }

                    data.forEach(order => {
                        const row = `
            <tr>
                <td>${order.id}</td>
                <td>${order.customerName}</td>
                <td>${order.customerPhone}</td>
                <td>${formatPrice(order.totalAmount)}</td>
                <td>${formatStatus(order.status)}</td>
                <td>${formatDate(order.createdAt)}</td>
                <td>${formatActions(order)}</td>
            </tr>
        `;
                        tbody.append(row);
                    });
                }

                // Format price
                function formatPrice(price) {
                    return new Intl.NumberFormat('vi-VN', {
                        style: 'currency',
                        currency: 'VND'
                    }).format(price);
                }

                // Format status
                function formatStatus(status) {
                    const statusMap = {
                        'PENDING': { text: 'Chờ xử lý', class: 'warning' },
                        'CONFIRMED': { text: 'Đã xác nhận', class: 'info' },
                        'DELIVERY': { text: 'Đang giao hàng', class: 'success' },
                        'CANCELLED': { text: 'Đã hủy', class: 'danger' }
                    };

                    const statusInfo = statusMap[status] || { text: status, class: 'secondary' };
                    return `<span class="badge badge-${statusInfo.class}">${statusInfo.text}</span>`;
                }

                // Format date
                function formatDate(dateString) {
                    const date = new Date(dateString);
                    return date.toLocaleDateString('vi-VN') + ' ' + date.toLocaleTimeString('vi-VN');
                }

                // Format actions
                function formatActions(order) {
                    let actions = `
        <button type="button" class="btn btn-sm btn-info me-1" onclick="viewOrderDetails(${order.id})" title="Xem chi tiết">
            <i class="fas fa-eye"></i>
        </button>
    `;

                    // Only show update button for orders that can be updated
                    if (order.status === 'PENDING' || order.status === 'CONFIRMED') {
                        actions += `
            <button type="button" class="btn btn-sm btn-warning" onclick="showUpdateStatusModal(${order.id}, '${order.status}')" title="Cập nhật trạng thái">
                <i class="fas fa-edit"></i>
            </button>
        `;
                    }

                    return actions;
                }

                // Filter by status
                function filterByStatus() {
                    const status = $('#statusFilter').val();
                    loadOrders(status);
                }

                // Search orders
                function searchOrders() {
                    const searchTerm = $('#searchInput').val().toLowerCase();
                    if (!searchTerm) {
                        populateTable(ordersData);
                        return;
                    }

                    const filteredData = ordersData.filter(order =>
                        order.customerName.toLowerCase().includes(searchTerm) ||
                        order.customerEmail.toLowerCase().includes(searchTerm) ||
                        order.customerPhone.includes(searchTerm)
                    );

                    populateTable(filteredData);
                    updateTotalCount(filteredData.length);
                }

                // Clear filters
                function clearFilters() {
                    $('#statusFilter').val('');
                    $('#searchInput').val('');
                    loadOrders();
                }

                // Refresh table
                function refreshTable() {
                    const status = $('#statusFilter').val();
                    loadOrders(status);
                    showAlert('success', 'Đã làm mới dữ liệu');
                }

                // Update total count
                function updateTotalCount(count) {
                    $('#totalOrders').text(`${count} đơn hàng`);
                }

                // View order details
                function viewOrderDetails(orderId) {
                    $.ajax({
                        url: `/api/orders/${orderId}`,
                        method: 'GET',
                        success: function (response) {
                            if (response.success) {
                                const order = response.data;
                                const detailsHtml = `
                    <div class="row">
                        <div class="col-md-6">
                            <h6><strong>Thông tin đơn hàng</strong></h6>
                            <p><strong>ID:</strong> ${order.id}</p>
                            <p><strong>Trạng thái:</strong> ${formatStatus(order.status)}</p>
                            <p><strong>Tổng tiền:</strong> ${formatPrice(order.totalAmount)}</p>
                            <p><strong>Ngày đặt:</strong> ${formatDate(order.createdAt)}</p>
                            <p><strong>Cập nhật lần cuối:</strong> ${formatDate(order.updatedAt)}</p>
                        </div>
                        <div class="col-md-6">
                            <h6><strong>Thông tin khách hàng</strong></h6>
                            <p><strong>Tên:</strong> ${order.customerName}</p>
                            <p><strong>Email:</strong> ${order.customerEmail}</p>
                            <p><strong>Số điện thoại:</strong> ${order.customerPhone}</p>
                            <p><strong>Địa chỉ giao hàng:</strong> ${order.shippingAddress}</p>
                        </div>
                    </div>
                    ${order.notes ? `<div class="mt-3"><h6><strong>Ghi chú:</strong></h6><p>${order.notes}</p></div>` : ''}
                `;

                                $('#orderDetailsContent').html(detailsHtml);
                                $('#orderDetailsModal').modal('show');
                            } else {
                                showAlert('error', 'Không thể tải chi tiết đơn hàng');
                            }
                        },
                        error: function () {
                            showAlert('error', 'Lỗi khi tải chi tiết đơn hàng');
                        }
                    });
                }

                // Show update status modal
                function showUpdateStatusModal(orderId, currentStatus) {
                    $('#updateOrderId').val(orderId);
                    $('#newStatus').empty();
                    $('#statusNotes').val('');

                    // Add available status options based on current status
                    const statusOptions = getAvailableStatusOptions(currentStatus);
                    statusOptions.forEach(option => {
                        $('#newStatus').append(`<option value="${option.value}">${option.text}</option>`);
                    });

                    $('#updateStatusModal').modal('show');
                }

                // Get available status options based on current status
                function getAvailableStatusOptions(currentStatus) {
                    const allOptions = [
                        { value: 'CONFIRMED', text: 'Xác nhận' },
                        { value: 'DELIVERY', text: 'Giao hàng' },
                        { value: 'CANCELLED', text: 'Hủy đơn' }
                    ];

                    switch (currentStatus) {
                        case 'PENDING':
                            return allOptions.filter(opt => opt.value === 'CONFIRMED' || opt.value === 'CANCELLED');
                        case 'CONFIRMED':
                            return allOptions.filter(opt => opt.value === 'DELIVERY' || opt.value === 'CANCELLED');
                        default:
                            return [];
                    }
                }

                // Update order status
                function updateOrderStatus() {
                    const orderId = $('#updateOrderId').val();
                    const newStatus = $('#newStatus').val();
                    const notes = $('#statusNotes').val();

                    if (!newStatus) {
                        showAlert('error', 'Vui lòng chọn trạng thái mới');
                        return;
                    }

                    const requestData = {
                        status: newStatus,
                        notes: notes
                    };

                    $.ajax({
                        url: `/api/orders/${orderId}`,
                        method: 'PUT',
                        contentType: 'application/json',
                        data: JSON.stringify(requestData),
                        success: function (response) {
                            if (response.success) {
                                $('#updateStatusModal').modal('hide');
                                showAlert('success', 'Cập nhật trạng thái thành công');
                                refreshTable();
                            } else {
                                showAlert('error', 'Cập nhật thất bại: ' + response.message);
                            }
                        },
                        error: function () {
                            showAlert('error', 'Lỗi khi cập nhật trạng thái');
                        }
                    });
                }

                // Show alert
                function showAlert(type, message) {
                    const alertClass = type === 'success' ? 'alert-success' : 'alert-danger';
                    const alertHtml = `
        <div class="alert ${alertClass} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;

                    // Remove existing alerts
                    $('.alert').remove();

                    // Add new alert at the top of container
                    $('.container-fluid').prepend(alertHtml);

                    // Auto-hide success alerts after 3 seconds
                    if (type === 'success') {
                        setTimeout(() => {
                            $('.alert-success').alert('close');
                        }, 3000);
                    }
                }

                // Add CSS for better styling
                const customCSS = `
<style>
.badge {
    font-size: 0.8em;
}
.btn-sm {
    padding: 0.25rem 0.5rem;
    font-size: 0.875rem;
}
.table td {
    vertical-align: middle;
}
.modal-body .row {
    margin-bottom: 1rem;
}
.alert {
    margin-bottom: 1rem;
}
</style>
`;

                $('head').append(customCSS);
            </script>
