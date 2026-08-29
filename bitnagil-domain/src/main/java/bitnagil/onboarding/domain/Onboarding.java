package bitnagil.onboarding.domain;

import bitnagil.entity.BaseTimeEntity;
import bitnagil.onboarding.domain.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE onboarding SET deleted_at = NOW() WHERE onboarding_id = ?")
@Where(clause = "deleted_at IS NULL")
public class Onboarding extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long onboardingId;

//    @Enumerated(EnumType.STRING)
//    @Column(columnDefinition = "varchar(40)") // mysql의 enum 타입을 사용하지 않도록 설정
    @NotNull
    private LocalTime timeSlot;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(40)")
    @NotNull
    private EmotionType emotionType;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(40)")
    @NotNull
    private RealOutingFrequency realOutingFrequency;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(40)")
    @NotNull
    private TargetOutingFrequency targetOutingFrequency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false)
    private Case resultCase;
}
