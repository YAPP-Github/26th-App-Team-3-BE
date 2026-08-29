package bitnagil.report.controller.spec;

import bitnagil.global.annotation.CurrentUser;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.global.swagger.ApiTags;
import bitnagil.report.dto.request.ReportRegisterRequest;
import bitnagil.report.dto.response.ReportDetailInfoResponse;
import bitnagil.report.dto.response.ReportInfoResponse;
import bitnagil.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = ApiTags.REPORT)
public interface ReportSpec {

    @ApiResponse(responseCode = "200", useReturnTypeSchema = true, content =
        @Content(mediaType = "application/json", examples = @ExampleObject(name = "성공 예시", value =
            "{\n \"code\": \"OK\",\n \"message\": \"등록되었습니다.\",\n \"data\": 1\n}")))
    @Operation(summary = "제보등록",
            description = "회원이 제보를 등록합니다.\n\n"
                + "발급된 presigned url 전체를 reportImageUrl에 담아서 보내주세요.\n\n"
                + "queryParam은 빼고 담아주세요.")
    CustomResponseDto<Long> registerReport(User user, ReportRegisterRequest request);

    @Operation(summary = "전체 제보 목록 조회",
        description = "전체 제보 목록을 조회합니다.")
    CustomResponseDto<ReportInfoResponse> getAllReportInfo(User user);

    @Operation(summary = "제보 기록 상세 조회",
        description = "제보 기록 상세 정보를 조회합니다.")
    CustomResponseDto<ReportDetailInfoResponse> getReportDetailInfo(User user, Long reportId);


    /* 추후에 변경을 고려해서 소스만 남겨놓음
    @Operation(summary = "제보 이미지 등록",
            description = "presigned URL을 통해 업로드된 이미지들의 URL을 제보 이미지로 등록합니다.")
    CustomResponseDto<Object> updateImages(Long reportId, List<String> urls);
    */
}
