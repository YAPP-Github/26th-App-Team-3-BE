package bitnagil.routineV2.repository;

import bitnagil.routineInfoV2.domain.RoutineInfoV2;
import bitnagil.routineV2.domain.RoutineV2;
import bitnagil.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineV2Repository extends JpaRepository<RoutineV2, Long> {

    /**
     * startDate와 endDate 사이에 있는 루틴을 조회하는 메서드입니다.
     * fetch join을 사용하여 RoutineInfoV2와 함께 조회합니다.
     */
    @Query("""
        select r from RoutineV2 r
        join fetch r.routineInfo ri
        where ri.user = :user
          and r.routineDate between :startDate and :endDate
    """)
    List<RoutineV2> findByUserAndDateRange(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 특정 사용자의 루틴을 ID로 조회하는 메서드입니다.
     */
    @Query("""
        select r from RoutineV2 r
        join fetch r.routineInfo ri
        where ri.user = :user
            and r.routineId = :routineId
    """)
    Optional<RoutineV2> findByUserAndRoutineId(
            @Param("user") User user,
            @Param("routineId") Long routineId
    );

    // 오늘 이후의 루틴 정보에 맞는 루틴 내역을 조회
    List<RoutineV2> findByRoutineInfoAndRoutineDateAfter(RoutineInfoV2 routineInfoV2, LocalDate date);

    /**
     *  다수의 루틴을 물리 삭제하기 위한 JPQL
     *  메모리 로드 비용 줄이기 위해 routineIds를 파라미터로 채택
     */
    @Modifying
    @Query("DELETE FROM RoutineV2 r WHERE r.routineId IN :ids")
    void deleteAllPhysicallyByIds(@Param("ids") List<Long> routineIds);

    /**
     *  단일 루틴을 물리 삭제하기 위한 JPQL
     *  메모리 로드 비용 줄이기 위해 routineIds를 파라미터로 채택
     */
    @Modifying
    @Query("DELETE FROM RoutineV2 r WHERE r.routineId = :id")
    void deletePhysicallyById(@Param("id") Long routineId);

    // startDate부터 endDate까지 routineDate 포함 데이터 모두 삭제
    @Modifying
    @Query("DELETE FROM RoutineV2 r WHERE r.routineDate BETWEEN :startDate AND :endDate AND "
        + "r.routineInfo.routineInfoId = :routineInfoId")
    void deleteByRoutineDateBetweenAndRoutineInfo(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("routineInfoId") Long routineInfoId);

    // 해당 유저가 특정 기간(연·월)에 완료(routineCompleteYn=true)한 "큰 루틴" 건수 (뱃지 월별 발급 판정용)
    @Query("""
        select count(r) from RoutineV2 r
        where r.routineInfo.user = :user
          and r.routineCompleteYn = true
          and r.routineDate between :start and :end
    """)
    long countCompletedByUserAndRoutineDateBetween(
        @Param("user") User user,
        @Param("start") LocalDate start,
        @Param("end") LocalDate end);

}
