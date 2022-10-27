package com.company.kerberos;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "jmix.kerberos.ldap-server")
@ConstructorBinding
public class LdapServerProperties {

    private String serverUrl;

    private String searchBase;

    private String searchFilter;

    private String keyStoreFilePath;

    private String keyStorePassword;

    public LdapServerProperties(String serverUrl,
                                String searchBase,
                                String searchFilter,
                                String keyStoreFilePath,
                                String keyStorePassword) {
        this.serverUrl = serverUrl;
        this.searchBase = searchBase;
        this.searchFilter = searchFilter;
        this.keyStoreFilePath = keyStoreFilePath;
        this.keyStorePassword = keyStorePassword;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getSearchBase() {
        return searchBase;
    }

    public String getSearchFilter() {
        return searchFilter;
    }

    public String getKeyStoreFilePath() {
        return keyStoreFilePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }
}
