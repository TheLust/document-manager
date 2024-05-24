package md.ceiti.backend.controller;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.dto.request.ProfileChangePasswordRequest;
import md.ceiti.backend.dto.request.ProfileUpdateRequest;
import md.ceiti.backend.dto.response.Profile;
import md.ceiti.backend.facade.AccountFacade;
import md.ceiti.backend.security.AccountDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "${api.url.base}/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final AccountFacade accountFacade;

    @GetMapping
    public ResponseEntity<Profile> getProfile(@AuthenticationPrincipal AccountDetails accountDetails) {
        return new ResponseEntity<>(
                accountFacade.getProfile(accountDetails),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<Profile> updateProfile(@AuthenticationPrincipal AccountDetails accountDetails,
                                                 @RequestBody ProfileUpdateRequest profileUpdateRequest,
                                                 BindingResult bindingResult) {
        return new ResponseEntity<>(
                accountFacade.updateProfile(accountDetails,
                        profileUpdateRequest,
                        bindingResult),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal AccountDetails accountDetails,
                                               @RequestBody ProfileChangePasswordRequest profileChangePasswordRequest,
                                               BindingResult bindingResult) {
        accountFacade.changePassword(accountDetails,
                profileChangePasswordRequest,
                bindingResult);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/image")
    public ResponseEntity<byte[]> getImage(@AuthenticationPrincipal AccountDetails accountDetails) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(accountFacade.getImage(accountDetails));
    }

    @PostMapping(value = "/change-image")
    public ResponseEntity<Profile> changeImage(@AuthenticationPrincipal AccountDetails accountDetails,
                                               @RequestPart("image") MultipartFile imageResource) {
        return new ResponseEntity<>(
                accountFacade.changeImage(accountDetails, imageResource),
                HttpStatus.OK
        );
    }
}
