package io.vidyo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Application extends AbstractAuditingEntity implements Serializable {

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

    @NotNull
    @Size(max = 255)
    @Column(name = "domain", length = 255, nullable = false)
    private String domain;

    @NotNull
    @Size(max = 255)
    @Column(name = "dev_key", length = 255, nullable = false)
    private String devKey;

    @Lob
    @Column(name = "idp_metadata")
    private String idpMetadata;

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

    public Application name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public Application key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDomain() {
        return domain;
    }

    public Application domain(String domain) {
        this.domain = domain;
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDevKey() {
        return devKey;
    }

    public Application devKey(String devKey) {
        this.devKey = devKey;
        return this;
    }

    public void setDevKey(String devKey) {
        this.devKey = devKey;
    }

    public String getIdpMetadata() {
        return idpMetadata;
    }

    public Application idpMetadata(String idpMetadata) {
        this.idpMetadata = idpMetadata;
        return this;
    }

    public void setIdpMetadata(String idpMetadata) {
        this.idpMetadata = idpMetadata;
    }

    public User getOwner() {
        return owner;
    }

    public Application owner(User user) {
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
        Application application = (Application) o;
        if (application.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, application.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Application{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", key='" + key + "'" +
            ", domain='" + domain + "'" +
            ", devKey='" + devKey + "'" +
            ", idpMetadata='" + idpMetadata + "'" +
            '}';
    }
}
