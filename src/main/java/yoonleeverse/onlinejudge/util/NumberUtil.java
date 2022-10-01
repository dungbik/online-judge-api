package yoonleeverse.onlinejudge.util;

public class NumberUtil {

    public static int toPage(Integer value) {
        return value == null || value <= 0 ? 0 : value - 1;
    }
}
