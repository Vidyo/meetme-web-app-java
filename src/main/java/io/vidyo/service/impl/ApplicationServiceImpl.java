package io.vidyo.service.impl;

import io.vidyo.service.ApplicationService;
import io.vidyo.domain.Application;
import io.vidyo.repository.ApplicationRepository;
import io.vidyo.service.dto.ApplicationDTO;
import io.vidyo.service.mapper.ApplicationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Application.
 */
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService{

    private final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private ApplicationMapper applicationMapper;

    /**
     * Save a application.
     *
     * @param applicationDTO the entity to save
     * @return the persisted entity
     */
    public ApplicationDTO save(ApplicationDTO applicationDTO) {
        log.debug("Request to save Application : {}", applicationDTO);
        Application application = applicationMapper.applicationDTOToApplication(applicationDTO);
        application = applicationRepository.save(application);
        ApplicationDTO result = applicationMapper.applicationToApplicationDTO(application);
        return result;
    }

    /**
     *  Get all the applications.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ApplicationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Applications");
        Page<Application> result = applicationRepository.findByOwnerIsCurrentUser(pageable);
        return result.map(application -> applicationMapper.applicationToApplicationDTO(application));
    }

    /**
     *  Get one application by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ApplicationDTO findOne(Long id) {
        log.debug("Request to get Application : {}", id);
        Application application = applicationRepository.findOne(id);
        ApplicationDTO applicationDTO = applicationMapper.applicationToApplicationDTO(application);
        return applicationDTO;
    }

    @Transactional(readOnly = true)
    public Optional<ApplicationDTO> findByKey(String key) {
        log.debug("Request to get Application : {}", key);
        Optional<Application> application = applicationRepository.findByKey(key);
        if (application.isPresent()) {
            Optional<ApplicationDTO> applicationDTO = Optional.ofNullable(applicationMapper.applicationToApplicationDTO(application.get()));
            return applicationDTO;
        }
        return Optional.empty();
    }


    /**
     *  Delete the  application by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Application : {}", id);
        applicationRepository.delete(id);
    }
}
