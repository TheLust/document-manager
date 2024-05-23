package md.ceiti.backend.facade;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.constant.ErrorCodes;
import md.ceiti.backend.dto.request.LoginRequest;
import md.ceiti.backend.exception.ApplicationException;
import md.ceiti.backend.security.AccountDetails;
import md.ceiti.backend.security.JwtUtils;
import md.ceiti.backend.service.impl.AccountService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public String login(LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        try {
            Authentication auth = authenticationManager.authenticate(authenticationToken);
            AccountDetails accountDetails = (AccountDetails) auth.getPrincipal();
            return jwtUtils.generateToken(accountDetails.getAccount());

        } catch (BadCredentialsException e) {
            throw new ApplicationException("Incorrect username or/and password", ErrorCodes.BAD_CREDENTIALS);
        }
    }
}
