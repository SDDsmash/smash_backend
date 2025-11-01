package SDD.smash.Apis.Controller;

import SDD.smash.Apis.Dto.CodeDTO;
import SDD.smash.Apis.Service.CodeService;
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
@RequestMapping("/api/code")
public class CodeController {

    private final CodeService codeService;

    @GetMapping("/jobTop")
    public ResponseEntity<List<CodeDTO>> getJobTops()
    {
        List<CodeDTO> codes = codeService.getAllJobTops();
        return ResponseEntity.ok(codes);
    }

    @GetMapping("/jobMid")
    public ResponseEntity<List<CodeDTO>> getJobMids(
            @RequestParam(required = true) @NotNull(message = "상위 직종코드는 필수입니다.") String topCode
            )
    {
        List<CodeDTO> codes = codeService.getAllJobMidsByTop(topCode);
        return ResponseEntity.ok(codes);
    }

    @GetMapping("/sido")
    public ResponseEntity<List<CodeDTO>> getSidos()
    {
        List<CodeDTO> codes = codeService.getAllSidos();
        return ResponseEntity.ok(codes);
    }

    @GetMapping("/sigungu")
    public ResponseEntity<List<CodeDTO>> getSigungus(
            @RequestParam(required = true) @NotNull(message = "시/도 코드는 필수입니다.") String sidoCode
    )
    {
        List<CodeDTO> codes = codeService.getAllSigungusBySido(sidoCode);
        return ResponseEntity.ok(codes);
    }

    @GetMapping("/supportTag")
    public ResponseEntity<List<CodeDTO>> getSupportTag()
    {
        List<CodeDTO> codes = codeService.getAllSupportTags();
        return ResponseEntity.ok(codes);
    }
}
