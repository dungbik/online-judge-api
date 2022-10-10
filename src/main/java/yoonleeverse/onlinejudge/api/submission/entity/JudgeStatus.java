package yoonleeverse.onlinejudge.api.submission.entity;

public enum JudgeStatus {
    PENDING(-2),
    WRONG_ANSWER(-1),
    SUCCESS(0),
    CPU_TIME_LIMIT_EXCEEDED(1),
    REAL_TIME_LIMIT_EXCEEDED(2),
    MEMORY_LIMIT_EXCEEDED(3),
    RUNTIME_ERROR(4),
    SYSTEM_ERROR(5),
    INIT_EVN_ERROR(6),
    COMPILE_ERROR(7),
    JUDGE_ERROR(8),
    UNK_ERROR(100);

    private int value;

    JudgeStatus(int value) {
        this.value = value;
    }

    public static JudgeStatus valueOf(int value) {
        for (JudgeStatus judgeStatus : JudgeStatus.values()) {
            if (judgeStatus.getValue() == value) {
                return judgeStatus;
            }
        }

        return UNK_ERROR;
    }

    public int getValue() {
        return this.value;
    }

}
