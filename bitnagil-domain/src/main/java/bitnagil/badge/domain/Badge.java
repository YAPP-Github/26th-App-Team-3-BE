package bitnagil.badge.domain;

import bitnagil.badge.domain.enums.BadgeType;
import bitnagil.user.domain.User;
import bitnagil.utils.YearMonthConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.time.YearMonth;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 사용자가 특정 활동(루틴 완료, 감정구슬 선택, 제보 등)을 그 달에 일정 횟수 이상 수행하면 수여되는 활동 뱃지입니다.
 * 뱃지는 매월 초기화되어(월별 발급) 같은 종류의 뱃지를 매달 다시 획득할 수 있습니다.
 * 한 번 수여되면 변경/삭제되지 않는 이력성 데이터이므로 {@code BaseTimeEntity}(수정/삭제 타임스탬프)를 상속하지 않고,
 * 물리적 기록 시각만 {@code createdAt}으로 감사(auditing)합니다. 뱃지가 귀속되는 연·월은 {@code badgeYearMonth}로 명시하며,
 * 이벤트 유실로 소급 발급될 때에도 실제 획득한 달을 가리키도록 {@code createdAt}과 분리합니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
    name = "badge",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_badge_user_year_month_type",
        columnNames = {"user_id", "badge_year_month", "badge_type"}
    )
)
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badgeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "badge_type", columnDefinition = "varchar(40)", nullable = false)
    private BadgeType badgeType;

    @Convert(converter = YearMonthConverter.class)
    @Column(name = "badge_year_month", columnDefinition = "varchar(7)", nullable = false)
    private YearMonth badgeYearMonth;

    @CreatedDate
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Builder(access = AccessLevel.PRIVATE)
    private Badge(User user, BadgeType badgeType, YearMonth badgeYearMonth) {
        this.user = user;
        this.badgeType = badgeType;
        this.badgeYearMonth = badgeYearMonth;
    }

    public static Badge grant(User user, BadgeType badgeType, YearMonth badgeYearMonth) {
        return Badge.builder()
            .user(user)
            .badgeType(badgeType)
            .badgeYearMonth(badgeYearMonth)
            .build();
    }
}
