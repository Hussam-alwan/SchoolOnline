package com.bootcamp.onlineschool.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditListener {

    @PrePersist
    public void onPrePersist(AuditableEntity entity) {
        LocalDateTime now = LocalDateTime.now();

        entity.setCreatedAt(now);
        entity.setCreatedBy("SYSTEM");
        entity.setUpdatedAt(now);
        entity.setUpdatedBy("SYSTEM");
    }

    @PreUpdate
    public void onPreUpdate(AuditableEntity entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy("SYSTEM");
    }
}

