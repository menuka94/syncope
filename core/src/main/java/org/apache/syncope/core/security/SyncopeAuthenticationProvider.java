/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.syncope.core.security;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;
import org.apache.syncope.core.audit.AuditManager;
import org.apache.syncope.core.persistence.beans.user.SyncopeUser;
import org.apache.syncope.core.persistence.dao.UserDAO;
import org.apache.syncope.types.CipherAlgorithm;
import org.apache.syncope.types.AuditElements.AuthenticationSubCategory;
import org.apache.syncope.types.AuditElements.Category;
import org.apache.syncope.types.AuditElements.Result;
import org.springframework.security.authentication.DisabledException;

@Configurable
public class SyncopeAuthenticationProvider implements AuthenticationProvider {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(SyncopeAuthenticationProvider.class);

    @Autowired
    private AuditManager auditManager;

    @Autowired
    private UserDAO userDAO;

    private SyncopeUserDetailsService userDetailsService;

    private String adminUser;

    private String adminMD5Password;

    public String getAdminMD5Password() {
        return adminMD5Password;
    }

    public void setAdminMD5Password(String adminMD5Password) {
        this.adminMD5Password = adminMD5Password;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public SyncopeUserDetailsService getSyncopeUserDetailsService() {
        return userDetailsService;
    }

    public void setSyncopeUserDetailsService(SyncopeUserDetailsService syncopeUserDetailsService) {

        this.userDetailsService = syncopeUserDetailsService;
    }

    @Override
    @Transactional(noRollbackFor = {BadCredentialsException.class, DisabledException.class})
    public Authentication authenticate(final Authentication authentication)
            throws AuthenticationException {

        boolean authenticated = false;
        SyncopeUser passwordUser = new SyncopeUser();
        SyncopeUser user = null;

        String username = authentication.getPrincipal().toString();

        if (adminUser.equals(username)) {
            passwordUser.setPassword(authentication.getCredentials().toString(), CipherAlgorithm.MD5, 0);
            authenticated = adminMD5Password.equalsIgnoreCase(passwordUser.getPassword());
        } else {
            user = userDAO.find(username);

            if (user != null) {
                if (user.getSuspended()) {
                    throw new DisabledException("User " + user.getUsername() + " is suspended");
                }

                passwordUser.setPassword(authentication.getCredentials().toString(), user.getCipherAlgoritm(), 0);
                authenticated = user.getPassword().equalsIgnoreCase(passwordUser.getPassword());
            }
        }

        UsernamePasswordAuthenticationToken token;

        if (authenticated) {
            token = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    null,
                    userDetailsService.loadUserByUsername(authentication.getPrincipal().toString()).getAuthorities());

            token.setDetails(authentication.getDetails());

            auditManager.audit(Category.authentication, AuthenticationSubCategory.login, Result.success,
                    "Successfully authenticated, with roles: " + token.getAuthorities());

            LOG.debug("User {} successfully authenticated, with roles {}", authentication.getPrincipal(), token.
                    getAuthorities());

            if (user != null) {
                user.setLastLoginDate(new Date());
                user.setFailedLogins(0);
                userDAO.save(user);
            }

        } else {
            if (user != null) {
                user.setFailedLogins(user.getFailedLogins() + 1);
                userDAO.save(user);
            }

            auditManager.audit(Category.authentication, AuthenticationSubCategory.login, Result.failure,
                    "User " + authentication.getPrincipal() + " not authenticated");

            LOG.debug("User {} not authenticated", authentication.getPrincipal());

            throw new BadCredentialsException("User " + authentication.getPrincipal() + " not authenticated");
        }

        return token;
    }

    @Override
    public boolean supports(final Class<? extends Object> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }
}
