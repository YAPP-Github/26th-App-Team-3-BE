package bitnagil.user.domain;

import bitnagil.onboarding.domain.Onboarding;
import bitnagil.user.domain.enums.Role;
import bitnagil.user.domain.enums.SocialType;
import bitnagil.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * 소셜 인증을 통해 가입한 사용자의 정보를 저장하는 JPA 엔티티 클래스입니다.
 * 소셜 타입, 소셜 ID, 이메일, 닉네임, 권한(Role) 등의 정보를 관리합니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE user SET role = 'WITHDRAWN', deleted_at = NOW() WHERE user_id = ?")
@Where(clause = "deleted_at IS NULL")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(40)")
    private SocialType socialType;

    @Column(nullable = false)
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(40)", nullable = false)
    private Role role;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    private String refreshToken; // 애플의 경우 탈퇴를 위한 필수값

    private Boolean agreedToTermsOfService; // 서비스 이용약관 동의
    private Boolean agreedToPrivacyPolicy; // 개인정보 수집 동의
    private Boolean isOverFourteen; // 14세 이상 여부

    private String reasonOfWithdrawal; // 탈퇴 사유

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onboarding_id")
    private Onboarding onboarding;

    @Builder
    public User(SocialType socialType, String socialId, Role role, String email, String nickname, String refreshToken) {
        this.socialType = socialType;
        this.socialId = socialId;
        this.role = role;
        this.email = email;
        this.nickname = nickname;
        this.refreshToken = refreshToken;
    }

    public void updateAgreements(Boolean agreedToTermsOfService, Boolean agreedToPrivacyPolicy, Boolean isOverFourteen) {
        this.agreedToTermsOfService = agreedToTermsOfService;
        this.agreedToPrivacyPolicy = agreedToPrivacyPolicy;
        this.isOverFourteen = isOverFourteen;
        this.role = Role.ONBOARDING; // 약관 동의 후 권한을 임시 USER인 ONBOARDING으로 변경
    }

    public void updateOnboarding(Onboarding onboarding) {
        this.onboarding = onboarding;
        this.role = Role.USER; // 온보딩 완료 후 권한을 USER로 변경
    }

    public void updateUserReasonOfWithdrawal(String reasonOfWithdrawal) {
        this.reasonOfWithdrawal = reasonOfWithdrawal;
    }

    // todo: 운영 반영 후 이슈가 없으면 제거
    public void changeRoleToWithdrawn() {
        this.role = Role.WITHDRAWN;
    }
}
