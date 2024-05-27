package md.ceiti.backend.controller.cms;


import lombok.RequiredArgsConstructor;
import md.ceiti.backend.dto.CmsAccountDto;
import md.ceiti.backend.dto.response.Profile;
import md.ceiti.backend.facade.AccountFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "${api.url.base}/cms/accounts")
@RequiredArgsConstructor
public class CmsAccountController {

    private final AccountFacade accountFacade;

    @GetMapping
    public ResponseEntity<List<CmsAccountDto>> findAll() {
        return new ResponseEntity<>(
                accountFacade.findAll(),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Long> insertGhost(@RequestBody CmsAccountDto accountDto,
                                            BindingResult bindingResult) {
        return new ResponseEntity<>(
                accountFacade.insertGhost(accountDto, bindingResult),
                HttpStatus.OK
        );
    }
}
