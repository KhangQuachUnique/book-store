// cartCountService.js - Quản lý số lượng items trong giỏ hàng (tương tự notificationService)

// Hàm lấy số lượng items từ server
async function fetchCartItemCount() {
    try {
        const base = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : '';
        const url = `${base}/user/cart?action=count`;
        const response = await fetch(url, { credentials: 'same-origin' });

        if (response.status === 401) {
            // Người dùng chưa đăng nhập
            console.log("User not logged in. Cannot fetch cart count.");
            updateCartBadge(0);
            return;
        }

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        if (data && data.success) {
            updateCartBadge(data.count || 0);
        }
    } catch (error) {
        console.error('Failed to fetch cart count:', error);
    }
}

// Hàm cập nhật giao diện badge giỏ hàng
function updateCartBadge(count) {
    const badges = document.querySelectorAll('.cart-count, .cart-badge, [data-cart-count]');
    badges.forEach(badge => {
        if (badge) {
            badge.textContent = count;
            // Hiệu ứng animation nhẹ
            badge.style.transform = 'scale(1.2)';
            setTimeout(() => {
                badge.style.transform = 'scale(1)';
            }, 200);
        }
    });
}

// Export global function để các trang khác có thể gọi
window.updateCartCount = function(count) {
    if (typeof count === 'number' && count >= 0) {
        updateCartBadge(count);
    } else {
        // Nếu không truyền count, fetch lại từ server
        fetchCartItemCount();
    }
};

// Chạy khi trang được tải xong
document.addEventListener('DOMContentLoaded', () => {
    // Chỉ fetch nếu có phần tử cart-count trên trang
    if (document.querySelector('.cart-count, .cart-badge, [data-cart-count]')) {
        fetchCartItemCount();
    }
});
// cartCountService.js - Quản lý số lượng items trong giỏ hàng (tương tự notificationService)

// Hàm lấy số lượng items từ server
async function fetchCartItemCount() {
    try {
        const base = (typeof window !== 'undefined' && window.APP_CONTEXT) ? window.APP_CONTEXT : '';
        const url = `${base}/user/cart?action=count`;
        const response = await fetch(url, { credentials: 'same-origin' });

        if (response.status === 401) {
            // Người dùng chưa đăng nhập
            console.log("User not logged in. Cannot fetch cart count.");
            updateCartBadge(0);
            return;
        }

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        if (data && data.success) {
            updateCartBadge(data.count || 0);
        }
    } catch (error) {
        console.error('Failed to fetch cart count:', error);
    }
}

// Hàm cập nhật giao diện badge giỏ hàng
function updateCartBadge(count) {
    const badges = document.querySelectorAll('.cart-count, .cart-badge, [data-cart-count]');
    badges.forEach(badge => {
        if (badge) {
            badge.textContent = count;
            // Hiệu ứng animation nhẹ
            badge.style.transform = 'scale(1.2)';
            setTimeout(() => {
                badge.style.transform = 'scale(1)';
            }, 200);
        }
    });
}

// Export global function để các trang khác có thể gọi
window.updateCartCount = function(count) {
    if (typeof count === 'number' && count >= 0) {
        updateCartBadge(count);
    } else {
        // Nếu không truyền count, fetch lại từ server
        fetchCartItemCount();
    }
};

// Chạy khi trang được tải xong
document.addEventListener('DOMContentLoaded', () => {
    // Chỉ fetch nếu có phần tử cart-count trên trang
    if (document.querySelector('.cart-count, .cart-badge, [data-cart-count]')) {
        fetchCartItemCount();
    }
});