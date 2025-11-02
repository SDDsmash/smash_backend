package SDD.smash.Dwelling.Batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class DwellingCacheCleaner implements JobExecutionListener {
    private final RedisTemplate<String, Object> redisTemplate;

    public DwellingCacheCleaner(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Dwelling Redis 캐시 초기화 시작");
        Set<String> keys = redisTemplate.keys("dwelling:score:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info(" Redis 캐시 {}개 삭제 완료", keys.size());
        } else {
            log.warn(" 삭제할 Redis 캐시 없음");
        }
    }
}
