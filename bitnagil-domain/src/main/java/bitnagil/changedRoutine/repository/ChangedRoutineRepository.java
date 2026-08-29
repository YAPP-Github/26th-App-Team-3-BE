package bitnagil.changedRoutine.repository;

import bitnagil.changedRoutine.domain.ChangedRoutine;
import bitnagil.entity.HistoryPk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChangedRoutineRepository extends JpaRepository<ChangedRoutine, HistoryPk> {

    Optional<ChangedRoutine> findByChangedRoutinePk(HistoryPk changedRoutinePk);

    List<ChangedRoutine> findByChangedRoutinePk_Id(UUID changedRoutineId);

    List<ChangedRoutine> findByUserIdAndDeletedAtIsNullAndHistoryStartDateTimeBeforeAndHistoryEndDateTimeGreaterThanEqualAndChangedRoutineDateBetween(
            Long userId,
            LocalDateTime now1,
            LocalDateTime now2,
            LocalDate startDate,
            LocalDate endDate
    );
}
