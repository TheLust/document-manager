package md.ceiti.backend.controller.institutions;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.dto.institution.InstitutionDto;
import md.ceiti.backend.facade.InstitutionFacade;
import md.ceiti.backend.security.AccountDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${api.url.base}/institutions/{institutionId}")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionFacade institutionFacade;

    @GetMapping
    public ResponseEntity<InstitutionDto> get(@AuthenticationPrincipal AccountDetails accountDetails,
                                              @PathVariable("institutionId") Long institutionId) {
        return new ResponseEntity<>(
                institutionFacade.get(institutionId, accountDetails.getAccount()),
                HttpStatus.OK
        );
    }
}
