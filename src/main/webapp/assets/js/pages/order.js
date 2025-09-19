// Cache DOM elements
const ordersTable = document.getElementById('ordersTable');
const statusFilter = document.getElementById('statusFilter');
const searchInput = document.getElementById('searchInput');
const refreshBtn = document.getElementById('refreshBtn');
const orderDetailModal = new bootstrap.Modal(document.getElementById('orderDetailModal'));
const updateStatusModal = new bootstrap.Modal(document.getElementById('updateStatusModal'));
const updateStatusBtn = document.getElementById('updateStatusBtn');

// Hold the current orders data
let ordersData = [];

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    loadOrders();
    setupEventListeners();
});

// Setup event listeners
function setupEventListeners() {
    statusFilter.addEventListener('change', loadOrders);
    searchInput.addEventListener('input', filterOrders);
    refreshBtn.addEventListener('click', loadOrders);
    updateStatusBtn.addEventListener('click', handleStatusUpdate);
}

// Load orders from server
async function loadOrders() {
    try {
        const status = statusFilter.value;
        const url = status ? `/api/orders?status=${status}` : '/api/orders';
        const response = await fetch(url);
        const data = await response.json();

        if (data.success) {
            ordersData = data.data;
            renderOrders(ordersData);
        } else {
            showAlert('error', data.message);
        }
    } catch (error) {
        console.error('Error loading orders:', error);
        showAlert('error', 'Failed to load orders');
    }
}

// Render orders to table
function renderOrders(orders) {
    const tbody = ordersTable.querySelector('tbody');
    tbody.innerHTML = orders.map(order => `
        <tr>
            <td>#${order.id}</td>
            <td>${order.user.name}</td>
            <td>$${order.totalAmount.toFixed(2)}</td>
            <td><span class="badge bg-${getStatusColor(order.status)}">${order.status}</span></td>
            <td>${order.paymentMethod}</td>
            <td>${formatDate(order.createdAt)}</td>
            <td>
                <button class="btn btn-sm btn-info me-1" onclick="showOrderDetail(${order.id})">
                    <i class="fas fa-eye"></i>
                </button>
                <button class="btn btn-sm btn-primary" onclick="showUpdateStatus(${order.id})">
                    <i class="fas fa-edit"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

// Filter orders based on search input
function filterOrders() {
    const searchTerm = searchInput.value.toLowerCase();
    const filteredOrders = ordersData.filter(order => 
        order.user.name.toLowerCase().includes(searchTerm) ||
        order.user.email.toLowerCase().includes(searchTerm) ||
        order.user.phone.toLowerCase().includes(searchTerm)
    );
    renderOrders(filteredOrders);
}

// Show order detail modal
async function showOrderDetail(orderId) {
    try {
        const response = await fetch(`/api/orders/${orderId}`);
        const data = await response.json();

        if (data.success) {
            const order = data.data;
            
            // Update modal content
            document.getElementById('modalOrderId').textContent = order.id;
            document.getElementById('modalCustomerName').textContent = order.user.name;
            document.getElementById('modalCustomerEmail').textContent = order.user.email;
            document.getElementById('modalCustomerPhone').textContent = order.user.phone;
            document.getElementById('modalOrderStatus').textContent = order.status;
            document.getElementById('modalPaymentMethod').textContent = order.paymentMethod;
            document.getElementById('modalCreatedAt').textContent = formatDate(order.createdAt);
            document.getElementById('modalTotalAmount').textContent = `$${order.totalAmount.toFixed(2)}`;

            // Render order items
            const itemsHtml = order.items.map(item => `
                <tr>
                    <td>
                        <img src="${item.book.thumbnailUrl}" alt="${item.book.title}" class="book-thumb me-2" width="30">
                        ${item.book.title}
                    </td>
                    <td>$${item.price.toFixed(2)}</td>
                    <td>${item.quantity}</td>
                    <td>$${(item.price * item.quantity).toFixed(2)}</td>
                </tr>
            `).join('');
            document.getElementById('modalOrderItems').innerHTML = itemsHtml;

            orderDetailModal.show();
        } else {
            showAlert('error', data.message);
        }
    } catch (error) {
        console.error('Error loading order details:', error);
        showAlert('error', 'Failed to load order details');
    }
}

// Show update status modal
function showUpdateStatus(orderId) {
    const order = ordersData.find(o => o.id === orderId);
    if (order) {
        document.getElementById('updateOrderId').value = orderId;
        document.getElementById('newStatus').value = order.status;
        updateStatusModal.show();
    }
}

// Handle status update
async function handleStatusUpdate() {
    const orderId = document.getElementById('updateOrderId').value;
    const newStatus = document.getElementById('newStatus').value;

    try {
        const response = await fetch(`/api/orders/${orderId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ status: newStatus })
        });

        const data = await response.json();

        if (data.success) {
            updateStatusModal.hide();
            loadOrders();
            showAlert('success', 'Order status updated successfully');
        } else {
            showAlert('error', data.message);
        }
    } catch (error) {
        console.error('Error updating order status:', error);
        showAlert('error', 'Failed to update order status');
    }
}

// Utility functions
function getStatusColor(status) {
    const colors = {
        'pending': 'warning',
        'paid': 'info',
        'shipped': 'primary',
        'completed': 'success',
        'cancelled': 'danger'
    };
    return colors[status] || 'secondary';
}

function formatDate(dateString) {
    return new Date(dateString).toLocaleString();
}

function showAlert(type, message) {
    // Implement your alert system here (e.g., using Bootstrap toasts or alerts)
    const alertClass = type === 'success' ? 'alert-success' : 'alert-danger';
    const alertHtml = `
        <div class="alert ${alertClass} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    const alertContainer = document.createElement('div');
    alertContainer.className = 'alert-container position-fixed top-0 end-0 p-3';
    alertContainer.innerHTML = alertHtml;
    document.body.appendChild(alertContainer);

    setTimeout(() => {
        alertContainer.remove();
    }, 5000);
}