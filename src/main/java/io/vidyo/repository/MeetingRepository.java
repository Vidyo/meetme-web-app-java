package io.vidyo.repository;

import io.vidyo.domain.Meeting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Meeting entity.
 */
@SuppressWarnings("unused")
public interface MeetingRepository extends JpaRepository<Meeting,Long> {

    @Query("select meeting from Meeting meeting where meeting.owner.login = ?#{principal.username}")
    List<Meeting> findByOwnerIsCurrentUser();

    @Query("select meeting from Meeting meeting where meeting.owner.login = ?#{principal.username}")
    Page<Meeting> findByOwnerIsCurrentUser(Pageable pageable);

    Optional<Meeting> findByKey(String key);
}
