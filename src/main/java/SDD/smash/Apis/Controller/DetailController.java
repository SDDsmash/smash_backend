package SDD.smash.Apis.Controller;

import SDD.smash.Apis.Dto.DetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DetailController {



    @GetMapping("/detail")
    public ResponseEntity<DetailDTO> recommend(
            @RequestParam(name = "sigunguCode", required = true) String sigunguCode,
            @RequestParam(name = "midJobCode", required = false) String midJobCode
    )
    {
//        recommendService.recommend(supportTag, midJobCode, dwellingType, price, infraImportance);
        return null;
    }
}
