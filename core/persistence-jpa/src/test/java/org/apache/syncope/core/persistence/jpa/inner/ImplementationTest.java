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
package org.apache.syncope.core.persistence.jpa.inner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.apache.syncope.common.lib.types.ImplementationEngine;
import org.apache.syncope.common.lib.types.ImplementationType;
import org.apache.syncope.core.persistence.api.dao.ImplementationDAO;
import org.apache.syncope.core.persistence.api.entity.Implementation;
import org.apache.syncope.core.persistence.jpa.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional("Master")
public class ImplementationTest extends AbstractTest {

    @Autowired
    private ImplementationDAO implementationDAO;

    @Test
    public void findAll() {
        List<Implementation> implementations = implementationDAO.findAll();
        assertFalse(implementations.isEmpty());

        assertEquals(16, implementations.size());

        implementations = implementationDAO.find(ImplementationType.PULL_ACTIONS);
        assertEquals(1, implementations.size());

        implementations = implementationDAO.find(ImplementationType.PROPAGATION_ACTIONS);
        assertEquals(1, implementations.size());

        implementations = implementationDAO.find(ImplementationType.TASKJOB_DELEGATE);
        assertEquals(5, implementations.size());

        implementations = implementationDAO.find(ImplementationType.REPORTLET);
        assertEquals(2, implementations.size());

        implementations = implementationDAO.find(ImplementationType.ACCOUNT_RULE);
        assertEquals(2, implementations.size());

        implementations = implementationDAO.find(ImplementationType.PASSWORD_RULE);
        assertEquals(3, implementations.size());

        implementations = implementationDAO.find(ImplementationType.VALIDATOR);
        assertEquals(1, implementations.size());

        implementations = implementationDAO.find(ImplementationType.PULL_CORRELATION_RULE);
        assertEquals(1, implementations.size());
    }

    @Test
    public void create() {
        Implementation impl = entityFactory.newEntity(Implementation.class);
        impl.setKey("new");
        impl.setEngine(ImplementationEngine.GROOVY);
        impl.setType(ImplementationType.VALIDATOR);
        impl.setBody("");

        Implementation actual = implementationDAO.save(impl);
        assertNotNull(actual);
        assertEquals(impl, actual);
    }
}
