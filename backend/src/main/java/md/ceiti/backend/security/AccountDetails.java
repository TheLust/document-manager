package md.ceiti.backend.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import md.ceiti.backend.model.Account;
import md.ceiti.backend.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AccountDetails implements UserDetails {

    private final Account account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(account.getRole().name()));
    }

    @Override
    public String getPassword() {
        return this.account.getPassword();
    }

    @Override
    public String getUsername() {
        return this.account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (Role.GHOST.equals(account.getRole())) {
            return false;
        }

        if (Role.MASTER.equals(account.getRole())) {
            return true;
        }

        if (Role.INSTITUTION_MASTER.equals(account.getRole())) {
            return account.isEnabled();
        }

        return account.isEnabled() && account.isActive();
    }
}
