package bitnagil.infrastructure.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignedUrlCommand {
    private final String prefix;
    private final String fileName;
}
