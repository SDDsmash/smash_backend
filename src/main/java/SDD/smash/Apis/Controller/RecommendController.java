package SDD.smash.Apis.Controller;

import SDD.smash.Apis.Dto.RecommendDTO;
import SDD.smash.Apis.Service.RecommendService;
import SDD.smash.Dwelling.Entity.DwellingType;
import SDD.smash.Infra.Entity.InfraImportance;
import SDD.smash.Support.domain.SupportTag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/recommend")
    public ResponseEntity<List<RecommendDTO>> recommend(
            @RequestParam(name = "supportTag", required = false) SupportTag supportTag,
            @RequestParam(name = "midJobCode", required = false) String midJobCode,
            @RequestParam(name = "dwellingType", required = true) @NotNull(message = "주거 유형은 필수입니다.") DwellingType dwellingType,
            @RequestParam(name = "price", required = true) @NotNull(message = "가격은 필수입니다.") Integer price,
            @RequestParam(name = "infraImportance", required = true) @NotNull(message = "인프라 중요도는 필수입니다.") InfraImportance infraImportance
            )
    {
        List<RecommendDTO> recommend = recommendService.recommend(supportTag, midJobCode, dwellingType, price, infraImportance);
        return ResponseEntity.ok(recommend);
    }
}
