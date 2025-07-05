package com.pitchain.notification.domain;

import com.pitchain.common.constant.NotificationType;
import com.pitchain.common.entity.BaseEntity;
import com.pitchain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    public static NotificationHistory of(Member receiver, NotificationType notificationType) {
        NotificationHistory notificationHistory = new NotificationHistory();
        notificationHistory.member = receiver;
        notificationHistory.notificationType = notificationType;
        return notificationHistory;
    }
}
