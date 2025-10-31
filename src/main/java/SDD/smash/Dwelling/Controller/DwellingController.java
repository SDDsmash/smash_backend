package SDD.smash.Dwelling.Controller;

import SDD.smash.Dwelling.Dto.ResponseDTO;
import SDD.smash.Dwelling.Service.DwellingStatesService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DwellingController {

    private final DwellingStatesService dwellingStatesService;

    public DwellingController(DwellingStatesService dwellingStatesService) {
        this.dwellingStatesService = dwellingStatesService;
    }

    @PostMapping(value = "/molit",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> getAptRentStats(
            @RequestParam String lawdCd,
            @RequestParam String dealYmd,
            @RequestParam(defaultValue = "1") int months
            ) throws IllegalAccessException {
        ResponseDTO responseDTO = dwellingStatesService.getStatsAndSave(lawdCd, dealYmd, months);
        return ResponseEntity.ok(responseDTO);
    }
}
