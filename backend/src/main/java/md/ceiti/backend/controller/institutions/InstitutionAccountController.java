package md.ceiti.backend.controller.institutions;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.dto.institution.AccountDto;
import md.ceiti.backend.facade.InstitutionFacade;
import md.ceiti.backend.security.AccountDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "${api.url.base}/institutions/{institutionId}/users")
@RequiredArgsConstructor
public class InstitutionAccountController {

    private final InstitutionFacade institutionFacade;

    @GetMapping
    public ResponseEntity<List<AccountDto>> findAllAccounts(@AuthenticationPrincipal AccountDetails accountDetails,
                                                            @PathVariable Long institutionId) {
        return new ResponseEntity<>(
                institutionFacade.findAllAccounts(institutionId, accountDetails.getAccount()),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<AccountDto> insertAccount(@AuthenticationPrincipal AccountDetails accountDetails,
                                                    @PathVariable Long institutionId,
                                                    @RequestBody AccountDto accountDto,
                                                    BindingResult bindingResult) {
        return new ResponseEntity<>(
                institutionFacade.insertAccount(institutionId,
                        accountDetails.getAccount(),
                        accountDto,
                        bindingResult),
                HttpStatus.OK
        );
    }

    @PutMapping
    public ResponseEntity<AccountDto> updateAccount(@AuthenticationPrincipal AccountDetails accountDetails,
                                                    @PathVariable Long institutionId,
                                                    @RequestParam("id") Long accountId,
                                                    @RequestBody AccountDto accountDto,
                                                    BindingResult bindingResult) {
        return new ResponseEntity<>(
                institutionFacade.updateAccount(institutionId,
                        accountDetails.getAccount(),
                        accountId,
                        accountDto,
                        bindingResult),
                HttpStatus.OK
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal AccountDetails accountDetails,
                                              @PathVariable Long institutionId,
                                              @RequestParam("id") Long accountId) {
        institutionFacade.deleteAccount(institutionId, accountDetails.getAccount(), accountId);
        return ResponseEntity.ok().build();
    }
}
