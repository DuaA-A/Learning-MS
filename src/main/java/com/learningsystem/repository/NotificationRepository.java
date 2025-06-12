package com.learningsystem.repository;

import com.learningsystem.entity.Notification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


@Repository
public class NotificationRepository {

    private final ConcurrentHashMap<Long, Notification> notificationStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0); // For auto-incrementing IDs

    // Save or update a notification
    public Notification save(Notification notification) {
        if (notification.getId() == null) {
            notification.setId(idGenerator.incrementAndGet());
        }
        notificationStore.put(notification.getId(), notification);
        return notification;
    }

    // Find notification by ID
    public Optional<Notification> findById(Long id) {
        return Optional.ofNullable( notificationStore.get(id));
    }

    // Find all unread notifications for a user
    public List<Notification> findByUserIdAndIsReadFalse(Long userId) {
        List<Notification> results = new ArrayList<>();
        for (Notification notification : notificationStore.values()) {
            if (notification.getUserId().equals(userId) && !notification.isRead()) {
                results.add(notification);
            }
        }
        return results;
    }

    // Find all notifications for a user
    public List<Notification> findByUserId(Long userId) {
        List<Notification> results = new ArrayList<>();
        for (Notification notification : notificationStore.values()) {
            if (notification.getUserId().equals(userId)) {
                results.add(notification);
            }
        }
        return results;
    }

    // Delete a notification by ID
    public void deleteById(Long id) {
        notificationStore.remove(id);
    }

    // Find all notifications
    public List<Notification> findAll() {
        return new ArrayList<>(notificationStore.values());
    }
}
