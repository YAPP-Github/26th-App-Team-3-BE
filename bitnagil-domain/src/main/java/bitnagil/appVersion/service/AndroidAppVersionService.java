package bitnagil.appVersion.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bitnagil.appVersion.repository.AndroidAppVersionRepository;
import bitnagil.appVersion.domain.AndroidAppVersion;
import bitnagil.appVersion.dto.response.ForceUpdateResponse;
import bitnagil.errorcode.ErrorCode;
import bitnagil.exception.CustomException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AndroidAppVersionService {

    private final AndroidAppVersionRepository androidAppVersionRepository;

    @Transactional(readOnly = true)
    public ForceUpdateResponse validateForceUpdateRequired(Integer clientMajor, Integer clientMinor) {

        AndroidAppVersion latestVersion = androidAppVersionRepository.findFirstByOrderByMajorDescMinorDesc()
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ANDROID_APP_VERSION));

        // major 비교
        if (clientMajor < latestVersion.getMajor()) {
            // major 버전이 최소 요구 major 버전보다 낮으면 강제 업데이트 필요
            return ForceUpdateResponse.builder()
                .forceUpdateYn(true)
                .build();
        }

        if (clientMajor.equals(latestVersion.getMajor())) {
            // major 같으면 minor 비교
            if (clientMinor < latestVersion.getMinor()) {
                return ForceUpdateResponse.builder()
                    .forceUpdateYn(true)
                    .build();
            }
        }

        // 강제 업데이트 필요하지 않은 경우
        return ForceUpdateResponse.builder()
            .forceUpdateYn(false)
            .build();
    }
}

