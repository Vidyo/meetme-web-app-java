package io.vidyo.security;


import io.vidyo.domain.Authority;
import io.vidyo.service.UserService;
import org.opensaml.xml.schema.impl.XSStringImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {

    @Inject
    UserService userService;


    public Object loadUserBySAML(SAMLCredential credential)
        throws UsernameNotFoundException {
        /*
        XSStringImpl email =
            (XSStringImpl) credential.getAttributes().stream()
                .filter(a -> a.getName().equals("email"))
                .findFirst().
                    orElseThrow(() -> new UsernameNotFoundException("email not found from assertion"))
                .getAttributeValues().get(0);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(email.getValue(), "",authorities);
        */

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        String email = credential.getNameID().getValue();

        // check for existing admin role in db
        Optional<io.vidyo.domain.User> existingRecord = userService.getUserWithAuthoritiesByLogin(email);
        if (existingRecord.isPresent()) {
            Set<Authority> existingAuthorities = existingRecord.get().getAuthorities();
            Authority adminAuthority = new Authority();
            adminAuthority.setName("ROLE_ADMIN");
            if (existingAuthorities.contains(adminAuthority)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
        }

        return new User(email, "",authorities);
    }

}
