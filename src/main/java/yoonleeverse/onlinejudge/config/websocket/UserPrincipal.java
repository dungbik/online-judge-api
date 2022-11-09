package yoonleeverse.onlinejudge.config.websocket;

import lombok.Getter;
import yoonleeverse.onlinejudge.util.StringUtil;

import java.security.Principal;

@Getter
public class UserPrincipal implements Principal {
    private final String id;
    private final String userKey;

    public UserPrincipal(String id) {
        this.id = id;
        this.userKey = StringUtil.encryptMD5(id);
    }

    @Override
    public String getName() {
        return this.userKey;
    }
}
