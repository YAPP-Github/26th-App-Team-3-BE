package bitnagil.youthPolicy.domain;

import bitnagil.entity.BaseTimeEntity;
import bitnagil.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자가 찜한 청년 공고입니다. 공고 원본은 저장하지 않고 공고 번호(plcyNo)만 저장합니다.
 * 찜 해제는 하드 삭제이며, 같은 공고를 다시 찜하면 새 행으로 등록됩니다(bookmarkId 재발급 → 최근순 유지).
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class YouthPolicyBookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String plcyNo;

    @Builder
    public YouthPolicyBookmark(User user, String plcyNo) {
        this.user = user;
        this.plcyNo = plcyNo;
    }
}
