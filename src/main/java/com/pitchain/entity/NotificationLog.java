package com.pitchain.entity;

import com.pitchain.common.constant.NotificationType;
import com.pitchain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private NotificationType notificationType;

    public static NotificationLog of(Member receiver, NotificationType notificationType) {
        NotificationLog notificationLog = new NotificationLog();
        notificationLog.member = receiver;
        notificationLog.notificationType = notificationType;
        return notificationLog;
    }
}
