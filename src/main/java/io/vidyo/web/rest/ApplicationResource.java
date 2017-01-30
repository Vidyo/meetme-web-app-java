package io.vidyo.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.vidyo.security.SecurityUtils;
import io.vidyo.service.ApplicationService;
import io.vidyo.service.UserService;
import io.vidyo.service.util.RandomUtil;
import io.vidyo.web.rest.util.HeaderUtil;
import io.vidyo.web.rest.util.PaginationUtil;
import io.vidyo.service.dto.ApplicationDTO;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Application.
 */
@RestController
@RequestMapping("/api")
public class ApplicationResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationResource.class);

    @Inject
    private ApplicationService applicationService;

    @Inject
    private UserService userService;

    /**
     * POST  /applications : Create a new application.
     *
     * @param applicationDTO the applicationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new applicationDTO, or with status 400 (Bad Request) if the application has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/applications")
    @Timed
    public ResponseEntity<ApplicationDTO> createApplication(@Valid @RequestBody ApplicationDTO applicationDTO) throws URISyntaxException {
        log.debug("REST request to save Application : {}", applicationDTO);
        if (applicationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("application", "idexists", "A new application cannot already have an ID")).body(null);
        }
        applicationDTO.setKey(RandomUtil.generateRandomAlphaNumeric(8));
        applicationDTO.setOwnerId(userService.getUserWithAuthorities().getId());
        ApplicationDTO result = applicationService.save(applicationDTO);
        return ResponseEntity.created(new URI("/api/applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("application", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /applications : Updates an existing application.
     *
     * @param applicationDTO the applicationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated applicationDTO,
     * or with status 400 (Bad Request) if the applicationDTO is not valid,
     * or with status 500 (Internal Server Error) if the applicationDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/applications")
    @Timed
    public ResponseEntity<ApplicationDTO> updateApplication(@Valid @RequestBody ApplicationDTO applicationDTO) throws URISyntaxException {
        log.debug("REST request to update Application : {}", applicationDTO);
        if (applicationDTO.getId() == null) {
            return createApplication(applicationDTO);
        }

        // do not update app key on update
        ApplicationDTO existing = applicationService.findOne(applicationDTO.getId());
        applicationDTO.setKey(existing.getKey());

        ApplicationDTO result = applicationService.save(applicationDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("application", applicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /applications : get all the applications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of applications in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/applications")
    @Timed
    public ResponseEntity<List<ApplicationDTO>> getAllApplications(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Applications");
        Page<ApplicationDTO> page = applicationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/applications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /applications/:id : get the "id" application.
     *
     * @param id the id of the applicationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the applicationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/applications/{id}")
    @Timed
    public ResponseEntity<ApplicationDTO> getApplication(@PathVariable Long id) {
        log.debug("REST request to get Application : {}", id);
        ApplicationDTO applicationDTO = applicationService.findOne(id);

        if (applicationDTO.getOwnerId().longValue() != userService.getUserWithAuthorities().getId().longValue()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return Optional.ofNullable(applicationDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /applications/:id : delete the "id" application.
     *
     * @param id the id of the applicationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/applications/{id}")
    @Timed
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        log.debug("REST request to delete Application : {}", id);

        ApplicationDTO applicationDTO = applicationService.findOne(id);
        if (applicationDTO.getOwnerId().longValue() != userService.getUserWithAuthorities().getId().longValue()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        applicationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("application", id.toString())).build();
    }

}
