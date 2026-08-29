package bitnagil.kpi.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class KpiQueryRepositoryImpl implements KpiQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long countDistinctUsersWithRoutineCompletionV1InPeriod(LocalDate start, LocalDate end) {
        Query q = entityManager.createNativeQuery("""
            SELECT COUNT(DISTINCT r.user_id) FROM routine_completion rc
            INNER JOIN routine r ON r.routine_id = rc.routine_id AND r.history_seq = rc.routine_history_seq
            WHERE rc.complete_yn = 1 AND rc.performed_date BETWEEN ?1 AND ?2
            """);
        q.setParameter(1, start);
        q.setParameter(2, end);
        return ((Number) q.getSingleResult()).longValue();
    }

    @Override
    public List<Long> findUserIdsWithRoutineCompletionWithin7DaysV1(
        LocalDateTime monthStart, LocalDateTime monthEnd) {
        Query q = entityManager.createNativeQuery("""
            SELECT DISTINCT u.user_id FROM user u
            INNER JOIN routine r ON r.user_id = u.user_id
            INNER JOIN routine_completion rc ON rc.routine_id = r.routine_id AND rc.routine_history_seq = r.history_seq AND rc.complete_yn = 1
            WHERE u.created_at >= ?1 AND u.created_at <= ?2
              AND rc.performed_date >= DATE(u.created_at) AND rc.performed_date <= DATE_ADD(DATE(u.created_at), INTERVAL 7 DAY)
            """);
        q.setParameter(1, monthStart);
        q.setParameter(2, monthEnd);
        return toLongList(q.getResultList());
    }

    @Override
    public List<Long> findUserIdsWithRoutineCompletionWithin7DaysV2(
        LocalDateTime monthStart, LocalDateTime monthEnd) {
        Query q = entityManager.createNativeQuery("""
            SELECT DISTINCT u.user_id FROM user u
            INNER JOIN routine_infov2 ri ON ri.user_id = u.user_id AND ri.deleted_at IS NULL
            INNER JOIN routinev2 r2 ON r2.routine_info_id = ri.routine_info_id AND r2.routine_complete_yn = 1
            WHERE u.created_at >= ?1 AND u.created_at <= ?2
              AND r2.routine_date >= DATE(u.created_at) AND r2.routine_date <= DATE_ADD(DATE(u.created_at), INTERVAL 7 DAY)
            """);
        q.setParameter(1, monthStart);
        q.setParameter(2, monthEnd);
        return toLongList(q.getResultList());
    }

    private static List<Long> toLongList(List<?> rows) {
        return rows.stream()
            .map(n -> ((Number) n).longValue())
            .collect(Collectors.toList());
    }

    @Override
    public long countDistinctUsersWithRoutineCompletionV2InPeriod(LocalDate start, LocalDate end) {
        Query q = entityManager.createNativeQuery("""
            SELECT COUNT(DISTINCT ri.user_id) FROM routinev2 r2
            INNER JOIN routine_infov2 ri ON ri.routine_info_id = r2.routine_info_id AND ri.deleted_at IS NULL
            WHERE r2.routine_complete_yn = 1 AND r2.routine_date BETWEEN ?1 AND ?2
            """);
        q.setParameter(1, start);
        q.setParameter(2, end);
        return ((Number) q.getSingleResult()).longValue();
    }

    @Override
    public long countDistinctUsersWithRoutineCompletionInPeriod(LocalDate start, LocalDate end) {
        Query q = entityManager.createNativeQuery("""
            SELECT COUNT(DISTINCT u_id) FROM (
              SELECT r.user_id AS u_id FROM routine_completion rc
              INNER JOIN routine r ON r.routine_id = rc.routine_id AND r.history_seq = rc.routine_history_seq
              WHERE rc.complete_yn = 1 AND rc.performed_date BETWEEN ?1 AND ?2
              UNION
              SELECT ri.user_id AS u_id FROM routinev2 r2
              INNER JOIN routine_infov2 ri ON ri.routine_info_id = r2.routine_info_id AND ri.deleted_at IS NULL
              WHERE r2.routine_complete_yn = 1 AND r2.routine_date BETWEEN ?1 AND ?2
            ) AS combined
            """);
        q.setParameter(1, start);
        q.setParameter(2, end);
        return ((Number) q.getSingleResult()).longValue();
    }

    @Override
    public long countDistinctUsersWithRoutineRegistrationInPeriod(
        LocalDateTime monthStart, LocalDateTime monthEnd) {
        Query q = entityManager.createNativeQuery("""
            SELECT COUNT(DISTINCT ri.user_id) FROM routine_infov2 ri
            WHERE ri.deleted_at IS NULL AND ri.created_at >= ?1 AND ri.created_at <= ?2
            """);
        q.setParameter(1, monthStart);
        q.setParameter(2, monthEnd);
        return ((Number) q.getSingleResult()).longValue();
    }

    @Override
    public long countDistinctUsersWithOutingCompletionInPeriod(LocalDate start, LocalDate end) {
        Query q = entityManager.createNativeQuery("""
            SELECT COUNT(DISTINCT ri.user_id) FROM routinev2 r2
            INNER JOIN routine_infov2 ri ON ri.routine_info_id = r2.routine_info_id AND ri.deleted_at IS NULL
            WHERE ri.recommended_routine_type = 'OUTING' AND r2.routine_complete_yn = 1
              AND r2.routine_date BETWEEN ?1 AND ?2
            """);
        q.setParameter(1, start);
        q.setParameter(2, end);
        return ((Number) q.getSingleResult()).longValue();
    }

    @Override
    public long countDistinctUsersWithEmotionInPeriod(LocalDate start, LocalDate end) {
        Query q = entityManager.createNativeQuery("""
            SELECT COUNT(DISTINCT em.user_id) FROM emotion_marble em
            WHERE em.deleted_at IS NULL AND em.date BETWEEN ?1 AND ?2
            """);
        q.setParameter(1, start);
        q.setParameter(2, end);
        return ((Number) q.getSingleResult()).longValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Long> findDistinctActiveUserIdsInPeriod(
        LocalDate start,
        LocalDate end,
        LocalDateTime monthStart,
        LocalDateTime monthEnd) {
        Query q = entityManager.createNativeQuery("""
            SELECT DISTINCT u_id FROM (
              SELECT r.user_id AS u_id FROM routine_completion rc
              INNER JOIN routine r ON r.routine_id = rc.routine_id AND r.history_seq = rc.routine_history_seq
              WHERE rc.complete_yn = 1 AND rc.performed_date BETWEEN ?1 AND ?2
              UNION
              SELECT ri.user_id AS u_id FROM routinev2 r2
              INNER JOIN routine_infov2 ri ON ri.routine_info_id = r2.routine_info_id AND ri.deleted_at IS NULL
              WHERE r2.routine_date BETWEEN ?1 AND ?2
              UNION
              SELECT ri.user_id AS u_id FROM routine_infov2 ri
              WHERE ri.deleted_at IS NULL AND ri.created_at >= ?3 AND ri.created_at <= ?4
              UNION
              SELECT em.user_id AS u_id FROM emotion_marble em
              WHERE em.deleted_at IS NULL AND em.date BETWEEN ?1 AND ?2
            ) AS active_users
            """);
        q.setParameter(1, start);
        q.setParameter(2, end);
        q.setParameter(3, monthStart);
        q.setParameter(4, monthEnd);
        List<Number> rows = q.getResultList();
        return toLongList(rows);
    }
}
