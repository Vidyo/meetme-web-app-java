package io.vidyo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;

/**
 * Properties specific to Vidyo.
 *
 * <p>
 *     Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "vidyo", ignoreUnknownFields = false)
public class VidyoProperties {

    private final Saml saml = new Saml();

    public Saml getSaml() {
        return saml;
    }

    public static class Saml {

        private String spEntityBaseURL = "";
        private String spEntityId = "";
        private String spAlias = "";
        private String idpMetadataXMLPath = "";
        private Keystore keystore = new Keystore();

        public String getSpEntityBaseURL() {
            return spEntityBaseURL;
        }

        public void setSpEntityBaseURL(String spEntityBaseURL) {
            this.spEntityBaseURL = spEntityBaseURL;
        }

        public String getSpEntityId() {
            return spEntityId;
        }

        public void setSpEntityId(String spEntityId) {
            this.spEntityId = spEntityId;
        }

        public String getSpAlias() {
            return spAlias;
        }

        public void setSpAlias(String spAlias) {
            this.spAlias = spAlias;
        }

        public String getIdpMetadataXMLPath() {
            return idpMetadataXMLPath;
        }

        public void setIdpMetadataXMLPath(String idpMetadataXMLPath) {
            this.idpMetadataXMLPath = idpMetadataXMLPath;
        }

        public Keystore getKeystore() {
            return keystore;
        }

        public void setKeystore(Keystore keystore) {
            this.keystore = keystore;
        }

        public static class Keystore {
            private String path = "";
            private String password = "";
            private String privateKeyAlias = "";
            private String privateKeyPassword= "";
            private String defaultPrivateKeyAlias= "";

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getPrivateKeyAlias() {
                return privateKeyAlias;
            }

            public void setPrivateKeyAlias(String privateKeyAlias) {
                this.privateKeyAlias = privateKeyAlias;
            }

            public String getPrivateKeyPassword() {
                return privateKeyPassword;
            }

            public void setPrivateKeyPassword(String privateKeyPassword) {
                this.privateKeyPassword = privateKeyPassword;
            }

            public String getDefaultPrivateKeyAlias() {
                return defaultPrivateKeyAlias;
            }

            public void setDefaultPrivateKeyAlias(String defaultPrivateKeyAlias) {
                this.defaultPrivateKeyAlias = defaultPrivateKeyAlias;
            }
        }
    }

}
