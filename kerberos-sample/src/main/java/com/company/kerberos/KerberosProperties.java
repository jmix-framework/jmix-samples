package com.company.kerberos;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "jmix.kerberos")
@ConstructorBinding
public class KerberosProperties {

    private String keytabFilePath;

    private String servicePrincipal;

    public KerberosProperties(String keytabFilePath,
                              String servicePrincipal) {
        this.keytabFilePath = keytabFilePath;
        this.servicePrincipal = servicePrincipal;
    }

    public String getKeytabFilePath() {
        return keytabFilePath;
    }

    public String getServicePrincipal() {
        return servicePrincipal;
    }
}
