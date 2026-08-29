package bitnagil.kpi.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface KpiQueryRepository {

    long countDistinctUsersWithRoutineCompletionV1InPeriod(LocalDate start, LocalDate end);

    long countDistinctUsersWithRoutineCompletionV2InPeriod(LocalDate start, LocalDate end);

    long countDistinctUsersWithRoutineCompletionInPeriod(LocalDate start, LocalDate end);

    long countDistinctUsersWithRoutineRegistrationInPeriod(LocalDateTime monthStart, LocalDateTime monthEnd);

    long countDistinctUsersWithOutingCompletionInPeriod(LocalDate start, LocalDate end);

    long countDistinctUsersWithEmotionInPeriod(LocalDate start, LocalDate end);

    List<Long> findDistinctActiveUserIdsInPeriod(
        LocalDate start,
        LocalDate end,
        LocalDateTime monthStart,
        LocalDateTime monthEnd);

    List<Long> findUserIdsWithRoutineCompletionWithin7DaysV1(LocalDateTime monthStart, LocalDateTime monthEnd);

    List<Long> findUserIdsWithRoutineCompletionWithin7DaysV2(LocalDateTime monthStart, LocalDateTime monthEnd);
}
