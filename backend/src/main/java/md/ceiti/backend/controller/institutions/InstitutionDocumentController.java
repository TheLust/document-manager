package md.ceiti.backend.controller.institutions;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.dto.institution.DocumentDto;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "${api.url.base}/institutions/{institutionId}/documents")
@RequiredArgsConstructor
public class InstitutionDocumentController {

    private final InstitutionFacade institutionFacade;

    @GetMapping
    public ResponseEntity<List<DocumentDto>> findAll(@AuthenticationPrincipal AccountDetails accountDetails,
                                                     @PathVariable Long institutionId) {
        return new ResponseEntity<>(
                institutionFacade.findAllDocuments(institutionId, accountDetails.getAccount()),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<DocumentDto> insert(@AuthenticationPrincipal AccountDetails accountDetails,
                                              @PathVariable Long institutionId,
                                              @RequestParam("category") Long categoryId,
                                              @RequestPart("document") MultipartFile multipartFile,
                                              @RequestPart("dto") DocumentDto documentDto,
                                              BindingResult bindingResult) {
        return new ResponseEntity<>(
                institutionFacade.insertDocument(institutionId,
                        accountDetails.getAccount(),
                        categoryId,
                        multipartFile,
                        documentDto,
                        bindingResult),
                HttpStatus.OK
        );
    }

    @PutMapping
    public ResponseEntity<DocumentDto> update(@AuthenticationPrincipal AccountDetails accountDetails,
                                              @PathVariable Long institutionId,
                                              @RequestParam("id") UUID documentId,
                                              @RequestParam("category") Long categoryId,
                                              @RequestBody DocumentDto documentDto,
                                              BindingResult bindingResult) {
        return new ResponseEntity<>(
                institutionFacade.updateDocument(institutionId,
                        accountDetails.getAccount(),
                        documentId,
                        categoryId,
                        documentDto,
                        bindingResult),
                HttpStatus.OK
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AccountDetails accountDetails,
                                       @PathVariable Long institutionId,
                                       @RequestParam("id") UUID id) {
        institutionFacade.deleteDocument(institutionId, accountDetails.getAccount(), id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@AuthenticationPrincipal AccountDetails accountDetails,
                                                   @PathVariable Long institutionId,
                                                   @PathVariable("id") UUID id) {
        byte[] bytes = institutionFacade.downloadDocument(institutionId, accountDetails.getAccount(), id);
        return ResponseEntity.ok(bytes);
    }
}
