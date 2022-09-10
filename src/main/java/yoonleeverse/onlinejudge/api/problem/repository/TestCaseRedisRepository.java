package yoonleeverse.onlinejudge.api.problem.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import yoonleeverse.onlinejudge.api.common.repository.RedisRepository;
import yoonleeverse.onlinejudge.api.problem.entity.TestCase;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TestCaseRedisRepository extends RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void save(long problemId, List<TestCase> testCases) {
        redisTemplate.opsForValue().set("problem:" + problemId, writeValue(testCases));
    }

    public List<TestCase> find(long problemId) {
        String json = redisTemplate.opsForValue().get("problem:" + problemId);
        if (json != null) {
            return readValue(json, new TypeReference<>() {});
        }
        return null;
    }

    public void delete(long problemId) {
        redisTemplate.delete("problem:" + problemId);
    }
}
