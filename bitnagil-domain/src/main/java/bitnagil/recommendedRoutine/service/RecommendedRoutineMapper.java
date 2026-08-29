package bitnagil.recommendedRoutine.service;

import bitnagil.emotionMarble.domain.EmotionMarble;
import bitnagil.recommendedRoutine.domain.RecommendedRoutine;
import bitnagil.recommendedRoutine.domain.RecommendedSubRoutine;
import bitnagil.recommendedRoutine.dto.response.RecommendedRoutineDto;
import bitnagil.recommendedRoutine.dto.response.RecommendedRoutineSearchResponse;
import bitnagil.recommendedRoutine.dto.response.RecommendedRoutineSearchResult;
import bitnagil.recommendedRoutine.dto.response.RecommendedSubRoutineSearchResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 추천 루틴 관련해서 DB에서 조회해오거나 가공된 데이터를 DTO로 변환하는 Mapper 클래스입니다.
 */
@Component
public class RecommendedRoutineMapper {

    // 추천 루틴을 DTO로 변환
    public RecommendedRoutineDto toRecommendedRoutineDto(RecommendedRoutine recommendedRoutine,
        List<RecommendedSubRoutineSearchResult> recommendedSubRoutineResults) {

        return RecommendedRoutineDto.builder()
            .recommendedRoutineId(recommendedRoutine.getRecommendedRoutineId())
            .recommendedRoutineName(recommendedRoutine.getRecommendedRoutineName())
            .recommendedRoutineDescription(recommendedRoutine.getRecommendedRoutineDescription())
            .recommendedSubRoutineSearchResult(recommendedSubRoutineResults)
            .build();
    }

    // 추천 루틴을 RecommendedRoutineSearchResult으로 변환
    public RecommendedRoutineSearchResult toRecommendedRoutineSearchResult(
        RecommendedRoutine recommendedRoutine, List<RecommendedSubRoutineSearchResult> recommendedSubRoutineResults) {

        return RecommendedRoutineSearchResult.builder()
            .recommendedRoutineId(recommendedRoutine.getRecommendedRoutineId())
            .recommendedRoutineName(recommendedRoutine.getRecommendedRoutineName())
            .recommendedRoutineDescription(recommendedRoutine.getRecommendedRoutineDescription())
            .recommendedRoutineLevel(recommendedRoutine.getRecommendedRoutineLevel())
            .executionTime(recommendedRoutine.getExecutionTime())
            .recommendedSubRoutineSearchResult(recommendedSubRoutineResults)
            .recommendedRoutineType(recommendedRoutine.getRecommendedRoutineType())
            .build();
    }

    // 추천 서브 루틴을 RecommendedSubRoutineSearchResult으로 변환
    public RecommendedSubRoutineSearchResult toRecommendedSubRoutineSearchResult(
        RecommendedSubRoutine recommendedSubRoutine) {

        return RecommendedSubRoutineSearchResult.builder()
            .recommendedSubRoutineId(recommendedSubRoutine.getRecommendedSubRoutineId())
            .recommendedSubRoutineName(recommendedSubRoutine.getSubRoutineName())
            .build();
    }

    // 추천 카테고리 별 루틴, 서브루틴을 반환하는 DTO로 변환
    public RecommendedRoutineSearchResponse toRecommendedRoutineSearchResponse(
        Map<String, List<RecommendedRoutineSearchResult>> response, EmotionMarble emotionMarble) {

        return RecommendedRoutineSearchResponse.builder()
            .recommendedRoutines(response)
            .emotionMarbleType(emotionMarble == null ? null : emotionMarble.getEmotionMarbleType()) // 감정 구슬 타입 설정
            .build();
    }
}
