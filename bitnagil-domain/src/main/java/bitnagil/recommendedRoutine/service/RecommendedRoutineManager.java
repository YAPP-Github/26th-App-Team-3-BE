package bitnagil.recommendedRoutine.service;

import bitnagil.onboarding.domain.Case;
import bitnagil.recommendedRoutine.domain.RecommendedRoutine;
import bitnagil.recommendedRoutine.dto.response.RecommendedRoutineDto;
import bitnagil.recommendedRoutine.dto.response.RecommendedSubRoutineSearchResult;
import bitnagil.recommendedRoutine.repository.RecommendedRoutineRepository;
import bitnagil.recommendedRoutine.repository.RecommendedSubRoutineRepository;
import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 외부에서 사용되는 추천 루틴에 대한 공통 로직을 관리하는 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class RecommendedRoutineManager {

    private final RecommendedRoutineRepository recommendedRoutineRepository;
    private final RecommendedSubRoutineRepository recommendedSubRoutineRepository;

    private final RecommendedRoutineMapper recommendedRoutineMapper;

    // 감정 구슬에 따른 추천 루틴 응답
    public List<RecommendedRoutineDto> recommendRoutinesByEmotionMarble(Case routineCase) {
        List<RecommendedRoutine> recommendedRoutines = recommendedRoutineRepository.findByResultCase(routineCase);
        if (recommendedRoutines.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_RECOMMENDED_ROUTINE);
        }

        return recommendedRoutines.stream() // recommendedRoutines를 순회하면서
            .map(this::toRecommendedRoutineDtoWithDetails) // 각 recommendedRoutine을 해당 메서드에 주입하여 수행하고
            .collect(Collectors.toList()); // 최종적으로 메서드의 반환 타입인 List<RecommendedRoutineDto>로 만들어줍니다.
    }

    // 추천 루틴에 대한 세부 정보를 반환하는 메서드
    private RecommendedRoutineDto toRecommendedRoutineDtoWithDetails(RecommendedRoutine recommendedRoutine) {

        List<RecommendedSubRoutineSearchResult> recommendedRoutineDetailDtoList =
            // 조회한 List<RecommendedSubRoutine>를 순회하면서
            recommendedSubRoutineRepository.findByRecommendedRoutine(recommendedRoutine).stream()
            .map(recommendedRoutineMapper::toRecommendedSubRoutineSearchResult) // 각 추천 서브루틴을 해당 Mapper를 통해 변환하고
            .toList(); // 이 정보들을 List<RecommendedSubRoutineSearchResult> 로 만들어줍니다.

        // 위에서 만든 추천 세부루틴 리스트와 추천 루틴을 Mapper를 통해 하나의 RecommendRoutineDto로 만들어줍니다.
        return recommendedRoutineMapper.toRecommendedRoutineDto(recommendedRoutine, recommendedRoutineDetailDtoList);
    }
}
