package bitnagil.userOnboardingInfo.domain;

import bitnagil.utils.StringListConverter;
import bitnagil.onboarding.domain.Onboarding;
import bitnagil.onboarding.domain.enums.TargetOutingFrequency;
import bitnagil.user.domain.User;
import bitnagil.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE user_onboarding_info SET role = 'WITHDRAWN', deleted_at = NOW() WHERE user_id = ?")
@Where(clause = "deleted_at IS NULL")
public class UserOnboardingInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userOnboardingInfoId;

    @NotNull
    private LocalTime timeSlot; // 사용자가 선택한 시간대

    @Convert(converter = StringListConverter.class)
    @NotNull
    private List<String> emotionTypes; // 사용자가 선택한 감정 유형 목록

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(40)")
    @NotNull
    private TargetOutingFrequency targetOutingFrequency; // 사용자가 선택한 목표 외출 빈도

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public UserOnboardingInfo(LocalTime timeSlot, List<String> emotionTypes, TargetOutingFrequency targetOutingFrequency,
                              Onboarding onboarding, User user) {
        this.timeSlot = timeSlot;
        this.emotionTypes = emotionTypes;
        this.targetOutingFrequency = targetOutingFrequency;
        this.user = user;
    }

    public void updateUserOnboardingInfo(LocalTime timeSlot, List<String> emotionType, TargetOutingFrequency targetOutingFrequency) {
        this.timeSlot = timeSlot;
        this.emotionTypes = emotionType;
        this.targetOutingFrequency = targetOutingFrequency;
    }
}
