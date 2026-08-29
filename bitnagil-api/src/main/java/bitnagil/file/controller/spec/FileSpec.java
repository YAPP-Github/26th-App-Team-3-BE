package bitnagil.file.controller.spec;

import bitnagil.file.request.PresignedUrlRequest;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.global.swagger.ApiTags;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Tag(name = ApiTags.FILE)
public interface FileSpec {

    @Operation(summary = "S3 Presigned URL 발급")
    CustomResponseDto<Map<String, String>> getPresignedUrls(List<PresignedUrlRequest> requests);
}
