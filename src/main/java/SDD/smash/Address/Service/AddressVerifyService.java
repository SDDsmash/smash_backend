package SDD.smash.Address.Service;

import SDD.smash.Address.Repository.SidoRepository;
import SDD.smash.Address.Repository.SigunguRepository;
import SDD.smash.Exception.Code.ErrorCode;
import SDD.smash.Exception.Exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressVerifyService {

    private final SidoRepository sidoRepository;
    private final SigunguRepository sigunguRepository;

    public void checkSigunguCodeOrThrow(String sigunguCode)
    {
        if(!sigunguRepository.existsBySigunguCode(sigunguCode))
            throw new BusinessException(ErrorCode.ADDRESS_CODE_NOT_FOUND, "유효하지 않은 시군구 코드");
    }
}
