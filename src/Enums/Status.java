package Enums;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Status {
    NEW("New"),
    IN_PROGRESS("In progress"),
    DONE("Done");

    private final String status;

    private static final Map<String, Status> STATUS_MAP =
            Stream.of(Status.values())
                    .collect(Collectors.toMap(Status::toString, e -> e));

    Status(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }

    public static Status fromString(String status) {
        return Optional.ofNullable(STATUS_MAP.get(status)).orElse(Status.NEW);
    }
}

