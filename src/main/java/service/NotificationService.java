package service;

import dao.NotificationDao;
import model.Notification;

import java.util.List;

/**
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
	 * Tạo thông báo mới cho người dùng
	 */
	public void createNotification(long userId, String title, String message) {
		try {
			notificationDao.createNotification(userId, title, message);
		} catch (Exception ex) {
			throw new RuntimeException("Lỗi khi tạo thông báo: " + ex.getMessage(), ex);
		}
	}

}
