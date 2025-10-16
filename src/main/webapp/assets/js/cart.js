// cart.js - Quản lý giỏ hàng với cập nhật real-time

document.addEventListener('DOMContentLoaded', function() {
    const BASE_URL = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : '';

    function formatVND(number) {
        return new Intl.NumberFormat('vi-VN').format(number) + ' ₫';
    }

    // Cập nhật summary (tổng tiền)
    function updateSummary(cartTotal) {
        const SHIPPING_FEE = 30000;
        document.querySelectorAll('.summary-row').forEach(function(row) {
            const label = row.querySelector('span:first-child');
            const value = row.querySelector('span:last-child');
            if (label && value) {
                const text = label.textContent.trim().toLowerCase();
                // Tìm theo tiếng Việt
                if (text.includes('tạm tính')) {
                    value.textContent = formatVND(cartTotal);
                } else if (text.includes('tổng cộng')) {
                    value.textContent = formatVND(cartTotal + SHIPPING_FEE);
                }
            }
        });
    }

    // Xử lý update quantity
    function handleUpdateQuantity(cartId, quantity, row) {
        fetch(`${BASE_URL}/user/cart`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: `action=update&cartId=${cartId}&quantity=${quantity}&ajax=true`
        })
            .then(res => {
                if (!res.ok) throw new Error('Network error');
                return res.json();
            })
            .then(data => {
                if (data && data.success) {
                    // Cập nhật tổng tiền của item
                    const totalCell = row.querySelector('td:nth-child(4) span');
                    if (totalCell) totalCell.textContent = formatVND(data.itemTotal);

                    // Cập nhật summary
                    updateSummary(data.cartTotal);

                    // Cập nhật badge ở header
                    if (typeof window.updateCartCount === 'function' && typeof data.count === 'number') {
                        window.updateCartCount(data.count);
                    }
                } else {
                    alert(data?.message || 'Cập nhật thất bại');
                }
            })
            .catch((err) => {
                console.error('Update error:', err);
                alert('Lỗi khi cập nhật giỏ hàng');
            });
    }

    // Xử lý remove item
    function handleRemoveItem(cartId, row) {
        if (!confirm('Bạn có chắc muốn xóa sản phẩm này?')) return;

        fetch(`${BASE_URL}/user/cart`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: `action=remove&cartId=${cartId}&ajax=true`
        })
            .then(res => {
                if (!res.ok) throw new Error('Network error');
                return res.json();
            })
            .then(data => {
                if (data && data.success) {
                    // Xóa row khỏi UI
                    row.remove();

                    // Cập nhật badge
                    if (typeof window.updateCartCount === 'function' && typeof data.count === 'number') {
                        window.updateCartCount(data.count);
                    }

                    // Kiểm tra nếu giỏ hàng trống
                    const tbody = document.querySelector('.cart-table tbody');
                    if (!tbody || tbody.children.length === 0) {
                        location.reload(); // Reload để hiển thị "giỏ hàng trống"
                    } else {
                        // Fetch lại để cập nhật tổng tiền
                        location.reload();
                    }
                } else {
                    alert(data?.message || 'Xóa thất bại');
                }
            })
            .catch((err) => {
                console.error('Remove error:', err);
                alert('Lỗi khi xóa sản phẩm');
            });
    }

    // Khởi tạo quantity controls
    document.querySelectorAll('.cart-quantity-input').forEach(function(input) {
        const form = input.closest('form');
        const row = input.closest('tr');
        if (!form || !row) return;

        const cartId = form.querySelector('input[name="cartId"]')?.value;
        if (!cartId) return;

        // Xóa nút update nếu có
        form.querySelector('.quantity-update-btn')?.remove();

        // Tạo custom quantity control
        const wrapper = document.createElement('div');
        wrapper.className = 'quantity-control';

        const minusBtn = document.createElement('button');
        minusBtn.type = 'button';
        minusBtn.textContent = '−';
        minusBtn.className = 'quantity-btn';

        const qtyInput = document.createElement('input');
        qtyInput.type = 'number';
        qtyInput.value = input.value;
        qtyInput.min = 1;
        qtyInput.className = 'cart-quantity-input';

        const plusBtn = document.createElement('button');
        plusBtn.type = 'button';
        plusBtn.textContent = '+';
        plusBtn.className = 'quantity-btn';

        wrapper.appendChild(minusBtn);
        wrapper.appendChild(qtyInput);
        wrapper.appendChild(plusBtn);

        // Replace form với control
        form.parentNode.replaceChild(wrapper, form);

        // Event listeners
        minusBtn.addEventListener('click', function() {
            let val = parseInt(qtyInput.value, 10);
            if (val > 1) {
                qtyInput.value = val - 1;
                handleUpdateQuantity(cartId, qtyInput.value, row);
            }
        });

        plusBtn.addEventListener('click', function() {
            let val = parseInt(qtyInput.value, 10);
            qtyInput.value = val + 1;
            handleUpdateQuantity(cartId, qtyInput.value, row);
        });

        qtyInput.addEventListener('change', function() {
            let val = parseInt(qtyInput.value, 10);
            if (isNaN(val) || val < 1) {
                qtyInput.value = 1;
                val = 1;
            }
            handleUpdateQuantity(cartId, val, row);
        });
    });

    // Khởi tạo remove buttons
    document.querySelectorAll('.cart-actions form').forEach(function(form) {
        const action = form.querySelector('input[name="action"]')?.value;
        if (action !== 'remove') return;

        const cartId = form.querySelector('input[name="cartId"]')?.value;
        const row = form.closest('tr');
        if (!cartId || !row) return;

        form.addEventListener('submit', function(e) {
            e.preventDefault();
            handleRemoveItem(cartId, row);
        });
    });
});