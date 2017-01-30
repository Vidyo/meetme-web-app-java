package io.vidyo.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;


/**
 * A DTO for the Application entity.
 */
public class ApplicationDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 64)
    private String key;

    @NotNull
    @Size(max = 255)
    private String domain;

    @NotNull
    @Size(max = 255)
    private String devKey;

    @Lob
    private String idpMetadata;


    private Long ownerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getDevKey() {
        return devKey;
    }

    public void setDevKey(String devKey) {
        this.devKey = devKey;
    }
    public String getIdpMetadata() {
        return idpMetadata;
    }

    public void setIdpMetadata(String idpMetadata) {
        this.idpMetadata = idpMetadata;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long userId) {
        this.ownerId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApplicationDTO applicationDTO = (ApplicationDTO) o;

        if ( ! Objects.equals(id, applicationDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ApplicationDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", key='" + key + "'" +
            ", domain='" + domain + "'" +
            ", devKey='" + devKey + "'" +
            ", idpMetadata='" + idpMetadata + "'" +
            '}';
    }
}
