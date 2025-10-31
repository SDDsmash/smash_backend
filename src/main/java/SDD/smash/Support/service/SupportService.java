package SDD.smash.Support.service;

import SDD.smash.Address.Service.AddressVerifyService;
import SDD.smash.Support.domain.SupportTag;
import SDD.smash.Support.dto.SupportDTO;
import SDD.smash.Support.dto.SupportListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final RedisTemplate<String, Object> redisTemplate;

    private AddressVerifyService addressVerifyService;

    /**
     * 해당 시군구의 모든 정책 개수 반환
     * 정책정보가 없는 경우 null
     */
    public Integer getAllSupportNum(String sigunguCode)
    {
        addressVerifyService.checkSigunguCodeOrThrow(sigunguCode);

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        Integer total = null;

        for(SupportTag tag : SupportTag.values())
        {
            String numKey = sigunguCode + ":" + tag.getValue() + "NUM";
            Object value = ops.get(numKey);
            if(value instanceof Number)
            {
                if(total == null) total = 0;
                total += ((Number)value).intValue();
            }
        }
        return total;
    }

    /**
     * 해당 시군구의 특정 태그의 정책 개수 반환
     * 정책정보가 없는 경우 null
     */
    public Integer getFitSupportNum(String sigunguCode, SupportTag tag)
    {
        addressVerifyService.checkSigunguCodeOrThrow(sigunguCode);

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        String numKey = sigunguCode + ":" + tag.getValue() + "NUM";
        Object value = ops.get(numKey);
        if(value instanceof Number)
        {
            return ((Number)value).intValue();
        }
        return null;
    }

    /**
     * 해당 시군구의 모든 정책 세부 정보 리스트 반환
     * 데이터가 없는 경우 null (!= 0)
     */
    public SupportListDTO getAllSupportList(String sigunguCode)
    {
        addressVerifyService.checkSigunguCodeOrThrow(sigunguCode);

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        SupportListDTO dto = null;

        for(SupportTag tag : SupportTag.values())
        {
            String baseKey = sigunguCode + ":" + tag.getValue();
            Object value = ops.get(baseKey);
            if(value instanceof SupportListDTO)
            {
                if(dto == null) dto = new SupportListDTO();
                List<SupportDTO> list = ((SupportListDTO) value).getSupportDTOList();
                dto.getSupportDTOList().addAll(list);
            }
        }
        return dto;
    }

//    /**
//     * 해당 시군구의 태그에 해당하는 정책 세부 정보 리스트 반환 (사용 가능성 없어 주석처리. 추후 제거)
//     * 데이터가 없는 경우 null (!= 0)
//     */
//    public SupportListDTO getFitSupportList(String sigunguCode, SupportTag tag)
//    {
//        addressVerifyService.checkSigunguCodeOrThrow(sigunguCode);
//
//        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
//
//        String baseKey = sigunguCode + ":" + tag.getValue();
//        Object value = ops.get(baseKey);
//        if(value instanceof SupportListDTO)
//        {
//            return (SupportListDTO) value;
//        }
//        return null;
//    }
}
