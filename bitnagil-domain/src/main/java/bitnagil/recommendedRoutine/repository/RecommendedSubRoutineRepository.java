package bitnagil.recommendedRoutine.repository;

import bitnagil.recommendedRoutine.domain.RecommendedRoutine;
import bitnagil.recommendedRoutine.domain.RecommendedSubRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendedSubRoutineRepository extends JpaRepository<RecommendedSubRoutine, Long> {
    List<RecommendedSubRoutine> findByRecommendedRoutine(RecommendedRoutine recommendedRoutine);
}
