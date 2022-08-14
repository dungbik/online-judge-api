package yoonleeverse.onlinejudge.api.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    @Test
    void findByProviderAndUserId() {
        System.out.println(userRepository.findByProviderAndUserId("GOOGLE", "112647067037091851573").orElse(null));
    }
}