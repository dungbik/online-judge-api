package yoonleeverse.onlinejudge.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yoonleeverse.onlinejudge.api.user.repository.UserRepository;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserComponent {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[가-힣|a-z|A-Z|0-9]+");

    private final UserRepository userRepository;

    /**
     * 이름 사용 가능 여부 체크
     * @param name 이름
     * @return 이름 사용 가능 여부
     */
    public boolean checkName(String name) {
        if (userRepository.existsByName(name)) {
            return false;
        }

        if (!NAME_PATTERN.matcher(name).matches()) {
            return false;
        }

        if (name.length() < 3 || name.length() > 10) {
            return false;
        }

        return true;
    }
}
