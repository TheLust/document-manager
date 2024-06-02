package md.ceiti.backend.controller.institutions;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.dto.institution.CategoryDto;
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
@RequestMapping(value = "${api.url.base}/institutions/{institutionId}/categories")
@RequiredArgsConstructor
public class InstitutionCategoryController {

    private final InstitutionFacade institutionFacade;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAllCategory(@AuthenticationPrincipal AccountDetails accountDetails,
                                                             @PathVariable Long institutionId) {
        return new ResponseEntity<>(
                institutionFacade.findAllCategories(institutionId, accountDetails.getAccount()),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<CategoryDto> insertCategory(@AuthenticationPrincipal AccountDetails accountDetails,
                                                      @PathVariable Long institutionId,
                                                      @RequestBody CategoryDto categoryDto,
                                                      BindingResult bindingResult) {
        return new ResponseEntity<>(
                institutionFacade.insertCategory(institutionId,
                        accountDetails.getAccount(),
                        categoryDto,
                        bindingResult),
                HttpStatus.OK
        );
    }

    @PutMapping
    public ResponseEntity<CategoryDto> updateCategory(@AuthenticationPrincipal AccountDetails accountDetails,
                                                      @PathVariable Long institutionId,
                                                      @RequestParam("id") Long categoryId,
                                                      @RequestBody CategoryDto categoryDto,
                                                      BindingResult bindingResult) {
        return new ResponseEntity<>(
                institutionFacade.updateCategory(institutionId,
                        accountDetails.getAccount(),
                        categoryId,
                        categoryDto,
                        bindingResult),
                HttpStatus.OK
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCategory(@AuthenticationPrincipal AccountDetails accountDetails,
                                               @PathVariable Long institutionId,
                                               @RequestParam("id") Long categoryId) {
        institutionFacade.deleteCategory(institutionId, accountDetails.getAccount(), categoryId);
        return ResponseEntity.ok().build();
    }
}
