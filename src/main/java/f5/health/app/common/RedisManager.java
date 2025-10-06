package f5.health.app.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisManager {

    private final RedisTemplate<String, String> redisTemplate;


    /** ValueOperations.. */

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long timeoutMills) {
        redisTemplate.opsForValue().set(key, value, Duration.ofMillis(timeoutMills));
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean isExists(String key) {
        return redisTemplate.hasKey(key);
    }

}
