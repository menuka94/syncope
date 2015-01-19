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
package org.apache.syncope.server.provisioning.api.sync;

import java.util.List;
import org.quartz.JobExecutionException;

public interface ProvisioningActions {

    /**
     * Action to be executed before to start the synchronization task execution.
     *
     * @param profile sync profile
     * @throws JobExecutionException in case of generic failure
     */
    void beforeAll(final ProvisioningProfile<?, ?> profile) throws JobExecutionException;

    /**
     * Action to be executed after the synchronization task completion.
     *
     * @param profile sync profile
     * @param results synchronization result
     * @throws JobExecutionException in case of generic failure
     */
    void afterAll(final ProvisioningProfile<?, ?> profile, final List<ProvisioningResult> results)
            throws JobExecutionException;
}