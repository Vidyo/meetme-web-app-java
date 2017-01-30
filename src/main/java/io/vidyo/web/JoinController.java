package io.vidyo.web;

import io.vidyo.domain.Application;
import io.vidyo.repository.ApplicationRepository;
import io.vidyo.repository.MeetingRepository;
import io.vidyo.service.ApplicationService;
import io.vidyo.service.MeetingService;
import io.vidyo.service.dto.ApplicationDTO;
import io.vidyo.service.util.GenerateToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class JoinController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ApplicationService applicationService;
    @Autowired
    MeetingService meetingService;

    @RequestMapping("/join/{appKey}/{resourceId}")
    String join(@PathVariable(value = "resourceId") String resourceId,
                @PathVariable(value="appKey") String appKey,
                Model model,
                HttpServletRequest request, HttpServletResponse response) {

        Optional<ApplicationDTO> app = applicationService.findByKey(appKey);
        if (!app.isPresent()) {
            log.info("Invalid join request for unknown app [" + appKey + "] from " + request.getRemoteAddr());
            response.setStatus(HttpStatus.NOT_FOUND.value());
            model.addAttribute("status", 404);
            model.addAttribute("error", "Not found");
            model.addAttribute("message", "The application was not found.");
            return "error";
        }

        if (!meetingService.findByKey(resourceId).isPresent()) {
            log.info("Invalid join request for unknown meeting [" + resourceId + "] from " + request.getRemoteAddr());
            response.setStatus(HttpStatus.NOT_FOUND.value());
            model.addAttribute("status", 404);
            model.addAttribute("error", "Not found");
            model.addAttribute("message", "The meeting was not found.");
            return "error";
        }


        model.addAttribute("token", generateToken(app.get().getDomain(), app.get().getDevKey()));
        model.addAttribute("resourceId", resourceId);

        log.info("Join request for [" + resourceId + "] from " + request.getRemoteAddr());

        return "VidyoConnector";
    }

    private String generateToken(String appId, String devKey) {
        String user =  java.util.UUID.randomUUID().toString().replaceAll("\\-","");
        return GenerateToken.generateProvisionToken(devKey, user + "@" + appId, 3600, "");
    }

}
