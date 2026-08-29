package bitnagil.onboarding.domain;

import bitnagil.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "routine_case") // 이렇게 예약어 회피
@SQLDelete(sql = "UPDATE routine_case SET deleted_at = NOW() WHERE case_id = ?")
@Where(clause = "deleted_at IS NULL")
public class Case extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caseId;

    @Builder
    public Case(Long caseId) {
        this.caseId = caseId;
    }
}
