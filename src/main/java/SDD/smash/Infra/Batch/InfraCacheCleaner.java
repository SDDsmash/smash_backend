package SDD.smash.Infra.Batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class InfraCacheCleaner implements JobExecutionListener {
    private final RedisTemplate<String, Object> redisTemplate;

    public InfraCacheCleaner(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        Set<String> keys = redisTemplate.keys("infra:score:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        } else {
            log.info(" 삭제할 Redis 캐시 없음");
        }
    }
}
