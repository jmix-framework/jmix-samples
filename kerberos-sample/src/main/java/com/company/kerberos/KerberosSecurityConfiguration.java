package com.company.kerberos;

import com.company.kerberos.ldap.ActiveDirectoryAuthoritiesPopulator;
import io.jmix.core.JmixOrder;
import io.jmix.core.security.UserRepository;
import io.jmix.core.security.impl.SystemAuthenticationProvider;
import io.jmix.security.role.ResourceRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.kerberos.authentication.KerberosAuthenticationProvider;
import org.springframework.security.kerberos.authentication.KerberosServiceAuthenticationProvider;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosClient;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator;
import org.springframework.security.kerberos.client.config.SunJaasKrb5LoginConfig;
import org.springframework.security.kerberos.client.ldap.KerberosLdapContextSource;
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter;
import org.springframework.security.kerberos.web.authentication.SpnegoEntryPoint;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class KerberosSecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(KerberosSecurityConfiguration.class);

    @Autowired
    private KerberosProperties kerberosProperties;

    @Autowired
    private LdapServerProperties ldapServerProperties;

    @Bean
    @Order(JmixOrder.HIGHEST_PRECEDENCE + 95)
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager kerberosAuthenticationManager,
                                                   SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter) throws Exception {
        http.authorizeRequests(authorize -> {
                    authorize.mvcMatchers("/login").permitAll()
                            .mvcMatchers("/vaadinServlet/PUSH").permitAll()
                            .anyRequest().authenticated();
                })
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint(spnegoEntryPoint());
                })
                .formLogin(formLogin -> {
                    formLogin.loginPage("/login");
                })
                .logout(logout -> {
                    logout.permitAll();
                })
                .csrf(csrf -> {
                    csrf.disable();
                })
                .authenticationManager(kerberosAuthenticationManager)
                .addFilterBefore(
                        spnegoAuthenticationProcessingFilter,
                        BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Primary
    public AuthenticationManager kerberosAuthenticationManager(KerberosAuthenticationProvider kerberosAuthenticationProvider,
                                                               KerberosServiceAuthenticationProvider kerberosServiceAuthenticationProvider,
                                                               UserRepository userRepository,
                                                               AuthenticationEventPublisher authenticationEventPublisher) {
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(kerberosAuthenticationProvider);
        providers.add(kerberosServiceAuthenticationProvider);
        providers.add(new SystemAuthenticationProvider(userRepository));
        ProviderManager providerManager = new ProviderManager(providers);
        providerManager.setAuthenticationEventPublisher(authenticationEventPublisher);
        return providerManager;
    }

    @Bean
    public SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter(AuthenticationManager kerberosAuthenticationManager) throws Exception {
        SpnegoAuthenticationProcessingFilter filter = new SpnegoAuthenticationProcessingFilter();
        filter.setAuthenticationManager(kerberosAuthenticationManager);
        return filter;
    }

    @Bean
    public KerberosAuthenticationProvider kerberosAuthenticationProvider(LdapUserDetailsService ldapUserDetailsService) {
        KerberosAuthenticationProvider provider = new KerberosAuthenticationProvider();
        SunJaasKerberosClient client = new SunJaasKerberosClient();
        client.setDebug(true);
        provider.setKerberosClient(client);
        provider.setUserDetailsService(ldapUserDetailsService);
        return provider;
    }

    @Bean
    public SpnegoEntryPoint spnegoEntryPoint() {
        return new SpnegoEntryPoint("/login");
    }

    @Bean
    public KerberosServiceAuthenticationProvider kerberosServiceAuthenticationProvider(LdapUserDetailsService ldapUserDetailsService) {
        KerberosServiceAuthenticationProvider provider = new KerberosServiceAuthenticationProvider();
        provider.setTicketValidator(sunJaasKerberosTicketValidator());
        provider.setUserDetailsService(ldapUserDetailsService);
        return provider;
    }

    @Bean
    public SunJaasKerberosTicketValidator sunJaasKerberosTicketValidator() {
        SunJaasKerberosTicketValidator ticketValidator = new SunJaasKerberosTicketValidator();
        ticketValidator.setServicePrincipal(kerberosProperties.getServicePrincipal());
        Resource fs = new FileSystemResource(kerberosProperties.getKeytabFilePath());
        log.info("Initializing Kerberos KEYTAB file path:" + fs.getFilename() + " for principal: " + kerberosProperties.getServicePrincipal() +
                "file exist: " + fs.exists());
        Assert.isTrue(fs.exists(), "*.keytab key must exist. Without that security is useless.");
        ticketValidator.setKeyTabLocation(fs);
        ticketValidator.setDebug(true);
        return ticketValidator;
    }

    @Bean
    public SunJaasKrb5LoginConfig sunJaasKrb5LoginConfig() {
        SunJaasKrb5LoginConfig loginConfig = new SunJaasKrb5LoginConfig();
        loginConfig.setKeyTabLocation(new FileSystemResource(kerberosProperties.getKeytabFilePath()));
        loginConfig.setServicePrincipal(kerberosProperties.getServicePrincipal());
        loginConfig.setDebug(true);
        loginConfig.setIsInitiator(true);
        return loginConfig;
    }

    @Bean
    public KerberosLdapContextSource kerberosLdapContextSource() {
        KerberosLdapContextSource contextSource = new KerberosLdapContextSource(ldapServerProperties.getServerUrl());
        contextSource.setLoginConfig(sunJaasKrb5LoginConfig());
        return contextSource;
    }

    @Bean
    LdapAuthoritiesPopulator authorities(ResourceRoleRepository resourceRoleRepository) {
        ActiveDirectoryAuthoritiesPopulator authoritiesPopulator = new ActiveDirectoryAuthoritiesPopulator(resourceRoleRepository);
        authoritiesPopulator.setGroupsAttribute("memberof");
        return authoritiesPopulator;
    }

    @Bean
    public LdapUserDetailsService ldapUserDetailsService(LdapAuthoritiesPopulator ldapAuthoritiesPopulator) {
        FilterBasedLdapUserSearch userSearch =
                new FilterBasedLdapUserSearch(ldapServerProperties.getSearchBase(),
                        ldapServerProperties.getSearchFilter(),
                        kerberosLdapContextSource());
        LdapUserDetailsService service = new LdapUserDetailsService(userSearch, ldapAuthoritiesPopulator);
        service.setUserDetailsMapper(new LdapUserDetailsMapper());
        return service;
    }
}
