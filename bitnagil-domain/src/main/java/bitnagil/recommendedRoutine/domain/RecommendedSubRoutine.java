package bitnagil.recommendedRoutine.domain;

import bitnagil.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE recommended_sub_routine SET deleted_at = NOW() WHERE recommended_sub_routine_id = ?")
@Where(clause = "deleted_at IS NULL")
public class RecommendedSubRoutine extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendedSubRoutineId;

    private String subRoutineName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommended_routine_id")
    private RecommendedRoutine recommendedRoutine;

    public void setRecommendedRoutine(RecommendedRoutine recommendedRoutine) {
        this.recommendedRoutine = recommendedRoutine;
    }
}
