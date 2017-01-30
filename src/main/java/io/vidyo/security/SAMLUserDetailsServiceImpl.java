package io.vidyo.security;


import org.opensaml.xml.schema.impl.XSStringImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {

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
        return new User(credential.getNameID().getValue(), "",authorities);
    }

}
