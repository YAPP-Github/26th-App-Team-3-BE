package bitnagil.recommendedRoutine.repository;

import bitnagil.onboarding.domain.Case;
import bitnagil.recommendedRoutine.domain.RecommendedRoutine;
import bitnagil.recommendedRoutine.domain.enums.RecommendedRoutineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendedRoutineRepository extends JpaRepository<RecommendedRoutine, Long> {
    List<RecommendedRoutine> findByResultCase(Case resultCase);

    List<RecommendedRoutine> findByRecommendedRoutineType(RecommendedRoutineType value);
}
