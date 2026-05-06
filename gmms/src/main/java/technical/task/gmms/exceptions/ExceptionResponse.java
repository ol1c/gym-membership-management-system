package technical.task.gmms.exceptions;

import java.time.Instant;

public record ExceptionResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {}