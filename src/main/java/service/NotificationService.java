package service;

import dao.NotificationDao;
import model.Notification;

import java.util.List;

/**
 * Service layer cho Notification
 * Xử lý business logic liên quan đến thông báo
 */
public class NotificationService {

	private final NotificationDao notificationDao = new NotificationDao();

	/**
	 * Đếm số thông báo chưa đọc của một người dùng
	 */
	public int countUnread(long userId) {
		return notificationDao.countUnreadByUserId(userId);
	}

	/**
	 * Lấy danh sách thông báo của một người dùng
	 */
	public List<Notification> listByUser(long userId) {
		return notificationDao.findByUserId(userId);
	}

	/**
	 * Đánh dấu tất cả thông báo của một người dùng là đã đọc
	 */
	public void markAllAsRead(long userId) {
		try {
			notificationDao.markAllAsRead(userId);
		} catch (Exception ex) {
			throw new RuntimeException("Lỗi khi đánh dấu thông báo đã đọc: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Tạo một thông báo mới cho người dùng
	 * @param userId ID người dùng
	 * @param title Tiêu đề thông báo
	 * @param message Nội dung thông báo
	 * @return Thông báo đã được tạo
	 */
	public Notification createForUser(long userId, String title, String message) {
		try {
			// Validate đầu vào
			if (title == null || title.trim().isEmpty()) {
				throw new IllegalArgumentException("Tiêu đề thông báo không được để trống");
			}
			
			return notificationDao.createForUser(userId, title, message);
		} catch (Exception ex) {
			throw new RuntimeException("Lỗi khi tạo thông báo: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Xóa một thông báo
	 * @param id ID thông báo cần xóa
	 */
	public void delete(Long id) {
		try {
			// Kiểm tra thông báo tồn tại
			Notification notification = notificationDao.findById(id);
			if (notification == null) {
				throw new IllegalArgumentException("Thông báo không tồn tại");
			}
			
			notificationDao.delete(id);
		} catch (Exception ex) {
			throw new RuntimeException("Lỗi khi xóa thông báo: " + ex.getMessage(), ex);
		}
	}
}
