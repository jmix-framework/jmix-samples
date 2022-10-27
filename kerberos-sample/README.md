# Kerberos Authentication

## Overview

The project demonstrates how to enable clients (browsers), authenticated in a Windows domain, to be transparently authenticated in Jmix application using Kerberos/SPNEGO without the need to type in a password again.

The project uses the [Spring Security Kerberos](https://docs.spring.io/spring-security-kerberos/docs/1.0.1.RELEASE/reference/htmlsingle/) library.

## Initial Setup

You must create an account in Active Directory for your application. For example, it may be something like `myserviceuser@DOMAIN.COM`.

The machine where your server is running must be a part of domain and should have a domain name assigned, e.g. `myservice.domain.com`.

You must configure the SPN (Service Principal Name) for your service in Active Directory and create a keytab file. The SPN may look like this: `HTTP/myservice.domain.com@DOMAIN.COM`

An SPN is a link between Active Directory user and your service. 

Keytab file contains the shared secret key of the service. The keytab file may be generated using the `ktpass` utility.

Read more in [Spring Security Kerberos documentation](https://docs.spring.io/spring-security-kerberos/docs/1.0.1.RELEASE/reference/htmlsingle/#setupwinkerberos).

## Configuration Properties

```properties
jmix.kerberos.keytab-file-path=C:\\keytabs\\myservice.keytab
jmix.kerberos.service-principal=HTTP/myservice.domain.com@DOMAIN.COM

# next properties must be defined if you need to query user details from LDAP once kerberos authentication  
# is successful and KerberosLdapContextSource bean is registered
jmix.kerberos.ldap-server.server-url=ldap://dc.domain.com:636
jmix.kerberos.ldap-server.search-base=dc=domain,dc=com
jmix.kerberos.ldap-server.search-filter=(|(userPrincipalName={0})(sAMAccountName={0}))

# next properties must be set if your active directory uses LDAPS protocol
jmix.kerberos.ldap-server.key-store-file-path=keystore/domain_key_store.jks
jmix.kerberos.ldap-server.key-store-password=password
```

## Client Browser Configuration

See https://docs.spring.io/spring-security-kerberos/docs/1.0.1.RELEASE/reference/htmlsingle/#browserspnegoconfig

## Implementation Details

The main class in the sample is the [KerberosSecurityConfiguration.java](src/main/java/com/company/kerberos/KerberosSecurityConfiguration.java). All Kerberos security is configured there. The configuration does the following:

* Defines `SecurityFilterChain` that processes all requests.
* For all unauthenticated requests the `SpnegoEntryPoint` is used. It sends the `WWW-Authenticate: Negotiate` header response back to the browser, so the browser initiates Kerberos authentication.
* Defines the `SpnegoAuthenticationProcessingFilter` that parses the SPNEGO authentication header.
* Defines the `AuthenticationManager` that uses kerberos-specific authentication providers.
* If SPNEGO authentication is not possible in the browser then the [login](src/main/resources/templates/login.html) page will be displayed where user may enter their domain username and password.

[ActiveDirectoryAuthoritiesPopulator.java](src/main/java/com/company/kerberos/ldap/ActiveDirectoryAuthoritiesPopulator.java) is used for mapping LDAP roles to Jmix resource roles. This class is just an example of how roles may be mapped. In your project you'll probably will have another implementation.

By default, SecurityContext will contain an instance of `LdapUserDetails` class. If you want an instance of another User type be in the security context (e.g. `User` entity generated in the project) then you may implement the `LdapUserDetailsMapper` in your project and use your implementation instead of `LdapUserDetailsMapper` when building the `LdapUserDetailsService` instance.

## LDAPS for Active Directory Server Configuration

If your active directory server uses LDAPS protocol, then you need to import Active Directory server certificate into Java keystore and provide a path and a password for this keystore.

Keystore may be created using the `keytool` utility.

The following code is added to the main Spring Boot application class:

```java
@EventListener
public void initKeyStore(ApplicationStartedEvent event) {
    if (!Strings.isNullOrEmpty(ldapServerProperties.getKeyStoreFilePath())) {
        System.setProperty("javax.net.ssl.trustStore", ldapServerProperties.getKeyStoreFilePath());
        System.setProperty("javax.net.ssl.trustStorePassword", ldapServerProperties.getKeyStorePassword());
    }
}
```

If `jmix.kerberos.ldap-server.key-store-file-path` configuration property is set then corresponding system properties will be set.

## Installation on Linux Server

### Troubleshooting 

In case you receive the `sun.security.krb5.KrbException: Encryption type RC4 with HMAC is not supported/enabled` error or similar while accessing the application you may do the following:

Create a `krb5.conf` file:

```
[libdefaults]
default_realm=YOURDOMAIN.COM
allow_weak_crypto=true
```

Run the application with the `-Djava.security.krb5.conf` parameter that points to the `krb5.conf` file:

```
java -Djava.security.krb5.conf=krb5.conf -jar app.jar
```