package bitnagil.youthPolicy.controller;

import bitnagil.global.annotation.CurrentUser;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.user.domain.User;
import bitnagil.youthPolicy.controller.spec.YouthPolicySpec;
import bitnagil.youthPolicy.dto.response.YouthPolicyListResponse;
import bitnagil.youthPolicy.service.YouthPolicyService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/youth-policies")
public class YouthPolicyController implements YouthPolicySpec {

    private final YouthPolicyService youthPolicyService;

    @GetMapping
    public CustomResponseDto<YouthPolicyListResponse> getYouthPolicies(
        @CurrentUser User user,
        @RequestParam @NotNull Double latitude,
        @RequestParam @NotNull Double longitude,
        @RequestParam(required = false) String cursor,
        @RequestParam(required = false) Integer size) {
        return CustomResponseDto.from(
            youthPolicyService.getYouthPolicies(user, latitude, longitude, cursor, size));
    }

    @GetMapping("/bookmarks")
    public CustomResponseDto<YouthPolicyListResponse> getBookmarkedYouthPolicies(
        @CurrentUser User user,
        @RequestParam(required = false) String cursor,
        @RequestParam(required = false) Integer size) {
        return CustomResponseDto.from(
            youthPolicyService.getBookmarkedYouthPolicies(user, cursor, size));
    }

    @PostMapping("/{plcyNo}/bookmark")
    public CustomResponseDto<Void> addBookmark(@CurrentUser User user, @PathVariable String plcyNo) {
        youthPolicyService.bookmark(user, plcyNo);
        return CustomResponseDto.from(null);
    }

    @DeleteMapping("/{plcyNo}/bookmark")
    public CustomResponseDto<Void> removeBookmark(@CurrentUser User user, @PathVariable String plcyNo) {
        youthPolicyService.unbookmark(user, plcyNo);
        return CustomResponseDto.from(null);
    }
}