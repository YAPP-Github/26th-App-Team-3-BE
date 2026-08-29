package bitnagil.file.controller;

import bitnagil.file.controller.spec.FileSpec;
import bitnagil.file.request.PresignedUrlRequest;
import bitnagil.global.response.CustomResponseDto;
import bitnagil.infrastructure.file.FileService;
import bitnagil.infrastructure.file.PresignedUrlCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v2/files")
public class FileController implements FileSpec {
    private final FileService fileService;

    // S3 Presigned URL 발급 API
    @PostMapping("/presigned-urls")
    public CustomResponseDto<Map<String, String>> getPresignedUrls(@RequestBody List<PresignedUrlRequest> requests) {
        List<PresignedUrlCommand> commands = requests.stream()
            .map(request -> new PresignedUrlCommand(request.getPrefix(), request.getFileName()))
            .collect(Collectors.toList());

        return CustomResponseDto.from(fileService.getPresignedUrls(commands));
    }
}
