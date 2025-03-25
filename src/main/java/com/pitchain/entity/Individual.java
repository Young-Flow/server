package com.pitchain.entity;

import com.pitchain.common.constant.OauthProvider;
import com.pitchain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table
public class Individual extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "individual_id")
    private Long id;

    private String socialId;

    @Enumerated(EnumType.STRING)
    private OauthProvider oauthProvider;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static Individual of(Member member, String socailId, OauthProvider oauthProvider) {
        return Individual.builder()
                .member(member)
                .socialId(builder().socialId)
                .oauthProvider(oauthProvider)
                .build();
    }

}
