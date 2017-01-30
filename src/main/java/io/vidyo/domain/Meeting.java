package io.vidyo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Meeting.
 */
@Entity
@Table(name = "meeting")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Meeting extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Size(max = 64)
    @Column(name = "key", length = 64, nullable = false)
    private String key;

    @ManyToOne
    private Application app;

    @ManyToOne
    private User owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Meeting name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public Meeting key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Application getApp() {
        return app;
    }

    public Meeting app(Application application) {
        this.app = application;
        return this;
    }

    public void setApp(Application application) {
        this.app = application;
    }

    public User getOwner() {
        return owner;
    }

    public Meeting owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Meeting meeting = (Meeting) o;
        if (meeting.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, meeting.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Meeting{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", key='" + key + "'" +
            '}';
    }
}
