package chess.view;

import java.util.Arrays;

public enum Command {
    START("start"),
    END("end"),
    MOVE("move"),
    STATUS("status");

    private final String displayName;

    Command(String displayName) {
        this.displayName = displayName;
    }

    public static Command findCommand(String displayName) {
        return Arrays.stream(values())
                .filter(command -> command.displayName.equals(displayName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 게임 명령어입니다."));
    }

    public boolean isStart() {
        return this == START;
    }

    public boolean isEnd() {
        return this == END;
    }

    public boolean isMove() {
        return this == MOVE;
    }

    public boolean isStatus() {
        return this == STATUS;
    }
}
