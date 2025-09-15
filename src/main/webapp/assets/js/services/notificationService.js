// Hàm để lấy số lượng thông báo chưa đọc từ server
async function fetchUnreadNotificationCount() {
    try {
        const response = await fetch('/book-store/api/notifications?action=getCount');
        
        if (response.status === 401) {
             // Người dùng chưa đăng nhập, không làm gì cả
            console.log("User not logged in. Cannot fetch notifications.");
            return;
        }

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        updateNotificationBell(data.unreadCount);

    } catch (error) {
        console.error('Failed to fetch notification count:', error);
    }
}

// Hàm để cập nhật giao diện chuông thông báo
function updateNotificationBell(count) {
    const bellCounter = document.getElementById('notification-counter');
    if (bellCounter) {
        if (count > 0) {
            bellCounter.textContent = count > 9 ? '9+' : count;
            bellCounter.style.display = 'flex'; // Hiển thị số đếm
        } else {
            bellCounter.style.display = 'none'; // Ẩn đi nếu không có thông báo
        }
    }
}

// Chạy hàm này khi trang được tải xong
document.addEventListener('DOMContentLoaded', () => {
    // Chỉ gọi khi có phần tử chuông trên trang (tức là user đã đăng nhập)
    if (document.getElementById('notification-bell')) {
        fetchUnreadNotificationCount();
    }
});