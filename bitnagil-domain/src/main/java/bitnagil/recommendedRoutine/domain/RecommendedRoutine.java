package bitnagil.recommendedRoutine.domain;

import bitnagil.onboarding.domain.Case;
import bitnagil.recommendedRoutine.domain.enums.Emotion;
import bitnagil.recommendedRoutine.domain.enums.RecommendedRoutineLevel;
import bitnagil.recommendedRoutine.domain.enums.RecommendedRoutineType;
import bitnagil.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE recommended_routine SET deleted_at = NOW() WHERE recommended_routine_id = ?")
@Where(clause = "deleted_at IS NULL")
public class RecommendedRoutine extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendedRoutineId;

    private String recommendedRoutineName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(40)")
    private RecommendedRoutineType recommendedRoutineType;

    private LocalTime executionTime;

    private String recommendedRoutineDescription;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(40)")
    private RecommendedRoutineLevel recommendedRoutineLevel;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(40)")
    private Emotion emotion;

    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private Case resultCase;
}
