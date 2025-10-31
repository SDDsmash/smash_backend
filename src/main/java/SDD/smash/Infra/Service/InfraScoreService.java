package SDD.smash.Infra.Service;

import SDD.smash.Infra.Entity.InfraImportance;
import SDD.smash.Infra.Entity.InfraScore;
import SDD.smash.Infra.Repository.InfraScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InfraScoreService {
    private static final String REDIS_KEY_PREFIX = "infra:score:";
    private final InfraScoreRepository infraScoreRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 인프라 중요도에 따라 점수를 보정해 전체 맵 반환
     */
    public Map<String, Integer> getInfraScoresByImportance(InfraImportance infraImportance)
    {
        String redisKey = REDIS_KEY_PREFIX + infraImportance.name();

        // 캐시 여부 확인
        var hashOps = redisTemplate.opsForHash();
        Map<Object, Object> cached = hashOps.entries(redisKey);
        if (cached != null && !cached.isEmpty()) {
            // 캐시에 있으면 변환해서 반환
            Map<String, Integer> result = new LinkedHashMap<>();
            for (Map.Entry<Object, Object> e : cached.entrySet()) {
                result.put((String) e.getKey(), (Integer) e.getValue());
            }
            return result;
        }

        // 캐시에 없으면 DB에서 조회
        List<InfraScore> scores = infraScoreRepository.findAllByOrderByScoreDesc();

        // 중요도에 따라 가공
        Map<String, Integer> processed = switch (infraImportance) {
            case MID -> applyMidRule(scores);
            case LOW -> applyLowRule(scores);
            case HIGH -> applyHighRule(scores); // 원본 그대로
        };

        // 캐싱
        hashOps.putAll(redisKey, new HashMap<>(processed));
         redisTemplate.expire(redisKey, Duration.ofHours(24));

        return processed;
    }


    /**
     * MID 규칙:
     * - 전체는 원래 점수 유지
     * - 단, "상위 21~200" 번째만 +30
     */
    private Map<String, Integer> applyMidRule(List<InfraScore> scores) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (int i = 0; i < scores.size(); i++) {
            InfraScore is = scores.get(i);
            int original = defaultScore(is.getScore());
            int finalScore = original;
            // index 0-based 이므로 21~200 → 20~199
            if (i >= 20 && i <= 199) {
                finalScore = original + 30;
            }
            result.put(is.getSigunguCode(), finalScore);
        }
        return result;
    }

    /**
     * LOW 규칙:
     * - 모두 0으로
     */
    private Map<String, Integer> applyLowRule(List<InfraScore> scores) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (InfraScore is : scores) {
            result.put(is.getSigunguCode(), 0);
        }
        return result;
    }

    /**
     * HIGH 규칙:
     * - 원본 그대로
     */
    private Map<String, Integer> applyHighRule(List<InfraScore> scores) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (InfraScore is : scores) {
            result.put(is.getSigunguCode(), defaultScore(is.getScore()));
        }
        return result;
    }

    private int defaultScore(Integer score) {
        return score != null ? score : 0;
    }

}
