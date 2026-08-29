package bitnagil.appVersion.controller.spec;

import bitnagil.appVersion.dto.response.ForceUpdateResponse;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.global.swagger.ApiTags;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = ApiTags.APP_VERSION)
public interface AndroidAppVersionSpec {

    @Operation(
        summary = "강제 업데이트 여부 검증",
        description = "사용자의 앱 Major, Minor, Patch 버전을 받아 강제 업데이트 필요 여부를 판단합니다."
    )
    @Parameters({
        @Parameter(name = "major", description = "앱 Major 버전", required = true, example = "1"),
        @Parameter(name = "minor", description = "앱 Minor 버전", required = true, example = "5"),
        @Parameter(name = "patch", description = "앱 Patch 버전 (추후 사용 예정)", required = true, example = "0")
    })
    CustomResponseDto<ForceUpdateResponse> validateForceUpdateRequired(int major, int minor, int patch);
}
