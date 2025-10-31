package SDD.smash.Support.service;

import SDD.smash.Address.Dto.SigunguCodeDTO;
import SDD.smash.Address.Repository.SigunguRepository;
import SDD.smash.Support.domain.SupportTag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SupportScoreService {

    private static final String REDIS_KEY_PREFIX = "support:score";

    private final RedisTemplate<String, Object> redisTemplate;
    private final SigunguRepository sigunguRepository;


    /**
     * 정책 태그에 따라 점수를 보정해 전체 맵 반환
     */
    public Map<String, Integer> getSupportScoresByTag(SupportTag tag)
    {
        String redisKey = REDIS_KEY_PREFIX + tag.getValue();
        var hashOps = redisTemplate.opsForHash();

        //캐시 확인
        Map<Object, Object> cached = hashOps.entries(redisKey);
        if (cached != null && !cached.isEmpty()) {
            // 이미 존재하면 캐시 리턴
            Map<String, Integer> result = new LinkedHashMap<>();
            for (Map.Entry<Object, Object> e : cached.entrySet()) {
                result.put((String) e.getKey(), (Integer) e.getValue());
            }
            return result;
        }

        //미스
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        List<SigunguCodeDTO> sigunguCodes = sigunguRepository.findAllSigunguCodes();
        Map<String, Integer> supportScoreMap = new LinkedHashMap<>();

        //각 시군구 코드에 대해 점수 계산
        for (SigunguCodeDTO dto : sigunguCodes)
        {
            String sigunguCode = dto.getSigunguCode();
            String baseKey = sigunguCode + ":" + tag.getValue() + ":NUM";

            Object val = valueOps.get(baseKey);

            int score;
            if (val == null) {
                score = 0; // 존재하지 않음
            } else {
                try {
                    int intVal = Integer.parseInt(val.toString());
                    score = intVal > 0 ? 100 : 0;
                } catch (NumberFormatException e) {
                    score = 0; // 값이 숫자가 아니면 0 처리
                }
            }
            supportScoreMap.put(sigunguCode, score);
        }
        hashOps.putAll(redisKey, new HashMap<>(supportScoreMap));
        redisTemplate.expire(redisKey, Duration.ofDays(4)); //만료 이전에 지원정책이 스케줄러에 의해 갱신된다면, 위 키는 제거됨

        return supportScoreMap;
    }
}
