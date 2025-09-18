<%--
  Created by IntelliJ IDEA.
  User: chien
  Date: 9/12/2025
  Time: 10:29 AM
  To change this template use File | Settings | File Templates.
--%>
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
                    <input type="text" id="searchInput" class="form-control" placeholder="Tìm theo tên khách hàng, email...">
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
                        <th data-field="totalAmount" data-sortable="true" data-formatter="priceFormatter">Tổng tiền</th>
                        <th data-field="status" data-sortable="true" data-formatter="statusFormatter">Trạng thái</th>
                        <th data-field="createdAt" data-sortable="true" data-formatter="dateFormatter">Ngày đặt</th>
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
<div class="modal fade" id="orderDetailsModal" tabindex="-1" aria-labelledby="orderDetailsModalLabel" aria-hidden="true">
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
<div class="modal fade" id="updateStatusModal" tabindex="-1" aria-labelledby="updateStatusModalLabel" aria-hidden="true">
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
                <button type="button" class="btn btn-primary" onclick="updateOrderStatus()">Cập nhật</button>
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

    $(document).ready(function() {
        loadOrders();
    });

    // Load orders from API
    function loadOrders(status = '') {
        const url = status ? `/api/orders?status=${status}` : '/api/orders';

        $.ajax({
            url: url,
            method: 'GET',
            success: function(response) {
                if (response.success) {
                    ordersData = response.data;
                    populateTable(ordersData);
                    updateTotalCount(ordersData.length);
                } else {
                    showAlert('error', 'Lỗi tải dữ liệu: ' + response.message);
                }
            },
            error: function() {
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
    function formatActions(
