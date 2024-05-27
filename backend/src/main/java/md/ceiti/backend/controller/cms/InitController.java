package md.ceiti.backend.controller.cms;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.model.Account;
import md.ceiti.backend.model.Role;
import md.ceiti.backend.service.impl.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "${api.url.base}/init")
@RequiredArgsConstructor
public class InitController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<String> init() {
        Account account = new Account();
        account.setUsername("admin");
        account.setPassword(passwordEncoder.encode("admin"));
        account.setFirstName("admin");
        account.setLastName("admin");
        account.setEmail("admin@admin.admin");
        account.setPhoneNumber("+37369696969");
        account.setBirthDate(LocalDate.now().minusDays(1));
        account.setEnabled(true);
        account.setRole(Role.MASTER);
        account.setInstitution(null);

        accountService.insert(account);

        return new ResponseEntity<>(
                "Initialized",
                HttpStatus.OK
        );
    }
}
