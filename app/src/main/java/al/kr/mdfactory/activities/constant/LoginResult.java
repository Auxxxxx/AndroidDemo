package al.kr.mdfactory.activities.constant;

import java.util.Arrays;
import java.util.Optional;

public enum LoginResult {
    OK(4),
    WRONG_PASSWORD(3),
    NO_EMPLOYEE(2),
    SHIFT_FINISHED(1);

    private final int code;

    LoginResult(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Optional<LoginResult> of(int code) {
        return Arrays.stream(values())
                .filter(option -> option.code == code)
                .findFirst();
    }
}
