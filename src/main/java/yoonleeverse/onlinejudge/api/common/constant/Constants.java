package yoonleeverse.onlinejudge.api.common.constant;

public class Constants {

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
}
