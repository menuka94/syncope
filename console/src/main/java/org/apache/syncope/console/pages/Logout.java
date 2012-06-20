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
package org.apache.syncope.console.pages;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.syncope.client.http.PreemptiveAuthHttpRequestFactory;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.syncope.console.SyncopeSession;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.web.client.RestTemplate;

/**
 * Syncope Logout.
 */
public class Logout extends BasePage {

    private static final long serialVersionUID = -2143007520243939450L;

    @SpringBean
    private RestTemplate restTemplate;

    public Logout(final PageParameters parameters) {
        super(parameters);

        SyncopeSession.get().invalidate();

        // TODO: check that this is indeed not necessary anymore.
        //        getRequestCycle().setRedirect(true);

        setResponsePage(getApplication().getHomePage());

        PreemptiveAuthHttpRequestFactory requestFactory =
                ((PreemptiveAuthHttpRequestFactory) restTemplate.getRequestFactory());

        ((DefaultHttpClient) requestFactory.getHttpClient()).getCredentialsProvider().clear();
    }
}
