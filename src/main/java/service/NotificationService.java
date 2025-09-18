package service;

import dao.NotificationDao;
import model.Notification;

import java.sql.SQLException;
import java.util.List;

public class NotificationService {
    private NotificationDao notificationDao = new NotificationDao();

    /**
     * Get count of unread notifications for a user
     */
    public int getUnreadNotificationCount(long userId) {
        return notificationDao.countUnreadByUserId(userId);
    }

    /**
     * Get all notifications for a user
     */
    public List<Notification> getAllNotifications(long userId) {
        return notificationDao.findByUserId(userId);
    }

    /**
     * Mark all notifications as read for a user
     */
    public void markAllNotificationsAsRead(long userId) {
        notificationDao.markAllAsRead(userId);
    }

    /**
     * Check if user has unread notifications
     */
    public boolean hasUnreadNotifications(long userId) {
        return getUnreadNotificationCount(userId) > 0;
    }
}
