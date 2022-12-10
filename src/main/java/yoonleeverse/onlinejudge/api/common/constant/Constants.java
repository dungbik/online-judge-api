package yoonleeverse.onlinejudge.api.common.constant;

public class Constants {

    public static final int PROBLEM_MAX_SIZE = 10;
    public static final int SUBMISSION_MAX_SIZE = 10;

    public enum ERole {
        ROLE_USER,
        ROLE_ADMIN
    }

    public class Cookie {
        public final static String REFRESH_TOKEN = "x-refresh";
    }

    public enum FileExtension {
        ZIP, PNG, JPG, JPEG
    }

    public enum WebSocketMessageType {
        JUDGE_RESULT,
        RUN_RESULT
    }
}
