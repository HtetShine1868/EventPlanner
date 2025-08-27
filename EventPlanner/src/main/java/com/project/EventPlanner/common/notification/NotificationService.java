package com.project.EventPlanner.common.notification;


import com.project.EventPlanner.features.user.domain.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Create a notification
    public NotificationDTO createNotification(User user, String message) {
        Notification notif = new Notification();
        notif.setUser(user);
        notif.setMessage(message);
        notif.setRead(false);
        notif.setCreatedAt(LocalDateTime.now()); // Make sure this is set
        notificationRepository.save(notif);
        return NotificationDTO.fromEntity(notif);
    }

    // Get all notifications for a user
    public List<NotificationDTO> getUserNotifications(User user) {
        List<Notification> notifs = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        return notifs.stream().map(NotificationDTO::fromEntity).collect(Collectors.toList());
    }

    // Mark notification as read
    public NotificationDTO markAsRead(Long notificationId) {
        Notification notif = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notif.setRead(true);
        notificationRepository.save(notif);
        return NotificationDTO.fromEntity(notif);
    }
}

