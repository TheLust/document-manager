package md.ceiti.backend.controller.cms;


import lombok.RequiredArgsConstructor;
import md.ceiti.backend.dto.cms.CmsAccountDto;
import md.ceiti.backend.facade.AccountFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<CmsAccountDto> insert(@RequestParam("institution") Long institutionId,
                                                @RequestBody CmsAccountDto accountDto,
                                                BindingResult bindingResult) {
        return new ResponseEntity<>(
                accountFacade.insert(institutionId, accountDto, bindingResult),
                HttpStatus.OK
        );
    }

    @PutMapping
    public ResponseEntity<CmsAccountDto> update(@RequestParam("id") Long id,
                                                @RequestParam("institution") Long institutionId,
                                                @RequestBody CmsAccountDto accountDto,
                                                BindingResult bindingResult) {
        return new ResponseEntity<>(
                accountFacade.update(id, institutionId, accountDto, bindingResult),
                HttpStatus.OK
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("id") Long id) {
        accountFacade.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ghost")
    public ResponseEntity<Long> insertGhost(@RequestBody CmsAccountDto accountDto,
                                            BindingResult bindingResult) {
        return new ResponseEntity<>(
                accountFacade.insertGhost(accountDto, bindingResult),
                HttpStatus.OK
        );
    }
}
