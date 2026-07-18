package bitnagil.emotionMarble.repository;

import bitnagil.emotionMarble.domain.EmotionMarble;
import bitnagil.emotionMarble.domain.enums.EmotionMarbleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmotionMarbleRepository extends JpaRepository<EmotionMarble, Long> {
    EmotionMarble findByUserId(Long userId);

    EmotionMarble findByUserIdAndDateIs(Long userId, LocalDate now);

    boolean existsByUserIdAndDate(Long userId, LocalDate nowDate);

    // 해당 유저가 특정 기간(연·월)에 선택한 감정 구슬 수 (뱃지 월별 발급 판정용)
    long countByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);

    @Query("SELECT COUNT(e) FROM EmotionMarble e WHERE e.date BETWEEN :start AND :end")
    long countByDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT COUNT(e) FROM EmotionMarble e WHERE e.date BETWEEN :start AND :end AND e.emotionMarbleType IN :types")
    long countByDateBetweenAndEmotionMarbleTypeIn(
        @Param("start") LocalDate start,
        @Param("end") LocalDate end,
        @Param("types") List<EmotionMarbleType> types);

    // 특정 유저가 해당 기간(연·월) 동안 선택한 감정 구슬을 날짜 오름차순으로 조회 (소프트삭제 @Where 자동 제외)
    List<EmotionMarble> findByUserIdAndDateBetweenOrderByDateAsc(Long userId, LocalDate start, LocalDate end);
}
