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
package org.apache.syncope.common.rest.api.service;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.syncope.common.lib.to.DynRealmTO;

/**
 * REST operations for dynamic realms.
 */
@Path("dynRealms")
public interface DynRealmService extends JAXRSService {

    /**
     * Returns a list of all dynamic realms.
     *
     * @return list of all dynamic realms.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    List<DynRealmTO> list();

    /**
     * Returns dynamic realm with matching key.
     *
     * @param key dynamic realm key to be read
     * @return dynamic realm with matching key
     */
    @GET
    @Path("{key}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    DynRealmTO read(@NotNull @PathParam("key") String key);

    /**
     * Creates a new dynamic realm.
     *
     * @param dynDynRealmTO dynamic realm to be created
     * @return Response object featuring Location header of created dynamic realm
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    Response create(@NotNull DynRealmTO dynDynRealmTO);

    /**
     * Updates the dynamic realm matching the provided key.
     *
     * @param dynDynRealmTO dynamic realm to be stored
     * @return an empty response if operation was successful
     */
    @PUT
    @Path("{key}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    Response update(@NotNull DynRealmTO dynDynRealmTO);

    /**
     * Deletes the dynamic realm matching the provided key.
     *
     * @param key dynamic realm key to be deleted
     * @return an empty response if operation was successful
     */
    @DELETE
    @Path("{key}")
    Response delete(@NotNull @PathParam("key") String key);

}
