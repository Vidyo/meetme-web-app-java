package io.vidyo.web;

import io.vidyo.domain.User;
import io.vidyo.service.UserService;
import io.vidyo.service.util.RandomUtil;
import net.logstash.logback.encoder.org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/saml")
public class SAMLController {
    private static final Logger log = LoggerFactory.getLogger(SAMLController.class);

    @Autowired
    private MetadataManager metadata;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/idpSelection", method = RequestMethod.GET)
    public String idpSelection(HttpServletRequest request, Model model) {
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            log.warn("The current user is already logged.");
            return "redirect:/";
        } else {
            if (isForwarded(request)) {
                Set<String> idps = metadata.getIDPEntityNames();
                for (String idp : idps)
                    log.info("Configured Identity Provider for SSO: " + idp);
                model.addAttribute("idps", idps);
                return "idpselection";
            } else {
                log.warn("Direct accesses to '/idpSelection' route are not allowed");
                return "redirect:/";
            }
        }
    }

    @RequestMapping(value = "/success")
    public String samlLoginSuccess() {

        // if user does not exist, provision them
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            String emailAddress = authentication.getName();
            Optional<User> user = userService.getUserWithAuthoritiesByLogin(emailAddress);
            if (!user.isPresent()) {
                String password = RandomUtil.generatePassword();
                String firstName = ""; // TODO: get from SAML attributes, if any
                String lastName = "" ;
                String language = "en";
                userService.createUser(emailAddress, password, firstName, lastName, emailAddress, language, true);
            } else {
                // TODO: can update user here, to get any changes to firstName/lastName/language
            }
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/failure")
    public String samlLoginFailure(HttpServletRequest request, Model model) {
        StringBuilder errorMessage = new StringBuilder("SAML Authentication Error");
        Exception samlException = (Exception) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        errorMessage.append(": ").append(samlException.getMessage());
        if (samlException.getCause() != null) {
            errorMessage.append("; ").append(samlException.getCause().getMessage());
            if (samlException.getCause().getCause() != null) {
                errorMessage.append("; ").append(samlException.getCause().getCause().getMessage());
            }
        }

        if (errorMessage.toString().contains("InResponseToField of the Response doesn't correspond")) {
            errorMessage.append(" -- ensure URL (protocol/hostname/port) used by SP, IdP and browser match (e.g. publichostname.com vs localhost vs 127.0.0.1)");
        }
        log.warn(errorMessage.toString());
        log.debug(ExceptionUtils.getStackTrace(samlException));

        model.addAttribute("status", 401);
        model.addAttribute("error", "Unauthorized");
        model.addAttribute("message", errorMessage.toString());
        return "error";
    }

    private boolean isForwarded(HttpServletRequest request){
        return request.getAttribute("javax.servlet.forward.request_uri") != null;
    }

}
