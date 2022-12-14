package groupId.artifactId.core.dto.output.crud;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.Instant;

@Builder
@Getter
@ToString
public class CompletedOrderDtoCrudOutput {
    private final @NonNull Long id;
    private final @NonNull Long ticketId;
    private final @NonNull Instant createdAt;
}
