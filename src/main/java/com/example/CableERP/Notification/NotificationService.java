package com.example.CableERP.Notification;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;


    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }


    public List<Notification> returnNotifications(boolean unreadOnly){
        return unreadOnly
                ? notificationRepository.findUnreadNotifications(NotificationStatus.UNREAD)
                : notificationRepository.findAll();
    }




}
