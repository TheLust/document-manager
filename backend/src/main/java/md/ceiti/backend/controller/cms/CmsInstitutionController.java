package md.ceiti.backend.controller.cms;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.dto.cms.CmsInstitutionDto;
import md.ceiti.backend.facade.InstitutionFacade;
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
@RequestMapping(value = "${api.url.base}/cms/institutions")
@RequiredArgsConstructor
public class CmsInstitutionController {

    private final InstitutionFacade institutionFacade;

    @GetMapping
    public ResponseEntity<List<CmsInstitutionDto>> findAll() {
        return new ResponseEntity<>(
                institutionFacade.findAll(),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<CmsInstitutionDto> insert(@RequestParam("master") Long masterId,
                                                    @RequestBody CmsInstitutionDto institutionDto,
                                                    BindingResult bindingResult) {
        return new ResponseEntity<>(
                institutionFacade.insert(masterId, institutionDto, bindingResult),
                HttpStatus.OK
        );
    }

    @PutMapping
    public ResponseEntity<CmsInstitutionDto> update(@RequestParam("id") Long id,
                                                    @RequestParam("master") Long masterId,
                                                    @RequestBody CmsInstitutionDto institutionDto,
                                                    BindingResult bindingResult) {
        return new ResponseEntity<>(
                institutionFacade.update(id, masterId, institutionDto, bindingResult),
                HttpStatus.OK
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("id") Long id) {
        institutionFacade.delete(id);
        return ResponseEntity.ok().build();
    }
}
