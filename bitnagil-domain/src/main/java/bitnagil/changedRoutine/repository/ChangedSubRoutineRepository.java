package bitnagil.changedRoutine.repository;

import bitnagil.changedRoutine.domain.ChangedSubRoutine;
import bitnagil.entity.HistoryPk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChangedSubRoutineRepository extends JpaRepository<ChangedSubRoutine, HistoryPk> {

    Optional<ChangedSubRoutine> findByChangedSubRoutinePk(HistoryPk changedSubRoutinePk);

    List<ChangedSubRoutine> findByChangedRoutineIdAndDeletedAtIsNullAndHistoryStartDateTimeBeforeAndHistoryEndDateTimeGreaterThanEqual(
            UUID changedRoutineId,
            LocalDateTime now1,
            LocalDateTime now2
    );
}
