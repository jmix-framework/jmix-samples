package com.company.kerberos.ldap;

import io.jmix.security.authentication.RoleGrantedAuthority;
import io.jmix.security.model.ResourceRole;
import io.jmix.security.role.ResourceRoleRepository;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import javax.naming.ldap.LdapName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class analyzes the value of the {@link #groupsAttribute} LDAP user attribute and tries to find JMix resource roles
 * with the same code. If the role is found then a {@link RoleGrantedAuthority} is created.
 */
public class ActiveDirectoryAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    private ResourceRoleRepository resourceRoleRepository;

    private String groupsAttribute = "memberOf";

    public ActiveDirectoryAuthoritiesPopulator(ResourceRoleRepository resourceRoleRepository) {
        this.resourceRoleRepository = resourceRoleRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        String[] groups = userData.getStringAttributes(groupsAttribute);
        if (groups == null) {
            return authorities;
        }
        for (String group : groups) {
            LdapName ldapName = LdapUtils.newLdapName(group);
            String groupName = (String) ldapName.getRdn(ldapName.size() - 1).getValue();
            authorities.add(new SimpleGrantedAuthority(groupName));
            ResourceRole role = resourceRoleRepository.findRoleByCode(groupName);
            if (role != null) {
                RoleGrantedAuthority roleGrantedAuthority = RoleGrantedAuthority.ofResourceRole(role);
                authorities.add(roleGrantedAuthority);
            }

        }
        return authorities;
    }

    public void setGroupsAttribute(String groupsAttribute) {
        this.groupsAttribute = groupsAttribute;
    }
}
