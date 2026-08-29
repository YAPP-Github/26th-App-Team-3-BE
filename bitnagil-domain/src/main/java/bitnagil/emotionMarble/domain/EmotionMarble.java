package bitnagil.emotionMarble.domain;

import bitnagil.emotionMarble.domain.enums.EmotionMarbleType;
import bitnagil.onboarding.domain.Case;
import bitnagil.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE emotion_marble SET deleted_at = NOW() WHERE emotion_marble_id = ?")
@Where(clause = "deleted_at IS NULL")
public class EmotionMarble extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emotionMarbleId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(40)")
    private EmotionMarbleType emotionMarbleType;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long userId;

    @NotNull
    private LocalDateTime historyStartDateTime;

    @NotNull
    private LocalDateTime historyEndDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false)
    private Case resultCase;

    @Builder
    public EmotionMarble(EmotionMarbleType emotionMarbleType, LocalDate date, Long userId,
                         LocalDateTime historyStartDateTime, LocalDateTime historyEndDateTime, Case resultCase) {
        this.emotionMarbleType = emotionMarbleType;
        this.date = date;
        this.userId = userId;
        this.historyStartDateTime = historyStartDateTime;
        this.historyEndDateTime = historyEndDateTime;
        this.resultCase = resultCase;
    }
}
