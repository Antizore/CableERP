package com.example.CableERP.Notification;


import jakarta.persistence.*;
import org.aspectj.weaver.ast.Not;

import java.sql.Timestamp;


@Entity
@Table(name = "alerts")
public class Notification {

    protected Notification(){}

    public Notification(NotificationCategory category, NotificationStatus status, Timestamp createdAt, String message) {
        this.category = category;
        this.status = status;
        this.createdAt = createdAt;
        this.message = message;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    NotificationCategory category;
    @Enumerated(EnumType.STRING)
    NotificationStatus status;
    Timestamp createdAt;
    Timestamp readAt;
    @Column(name = "message", length = 500)
    String message;


    public Long getId() {
        return id;
    }


    public NotificationCategory getCategory() {
        return category;
    }

    public void setCategory(NotificationCategory category) {
        this.category = category;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getReadAt() {
        return readAt;
    }

    public void setReadAt(Timestamp readAt) {
        this.readAt = readAt;
    }

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}
}
