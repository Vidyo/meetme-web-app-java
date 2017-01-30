package io.vidyo.repository;

import io.vidyo.domain.Application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Application entity.
 */
@SuppressWarnings("unused")
public interface ApplicationRepository extends JpaRepository<Application,Long> {

    @Query("select application from Application application where application.owner.login = ?#{principal.username}")
    List<Application> findByOwnerIsCurrentUser();

    @Query("select application from Application application where application.owner.login = ?#{principal.username}")
    Page<Application> findByOwnerIsCurrentUser(Pageable pageable);

    Optional<Application> findByKey(String key);

}
