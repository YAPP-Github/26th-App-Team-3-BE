package bitnagil.appVersion.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bitnagil.appVersion.controller.spec.AndroidAppVersionSpec;
import bitnagil.appVersion.dto.response.ForceUpdateResponse;
import bitnagil.appVersion.service.AndroidAppVersionService;
import bitnagil.global.response.CustomResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/version")
public class AndroidAppVersionController implements AndroidAppVersionSpec {

    private final AndroidAppVersionService androidAppVersionService;

    @GetMapping("/android/check")
    public CustomResponseDto<ForceUpdateResponse> validateForceUpdateRequired(
        @RequestParam int major,
        @RequestParam int minor,
        // 추후에 patch를 최소 버전 기준에 추가될 때 사용하기 위함
        @RequestParam int patch) {

        return CustomResponseDto.from(androidAppVersionService.validateForceUpdateRequired(major, minor));
    }
}
