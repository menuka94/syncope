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
package org.apache.syncope.core.persistence.jpa.validation.entity;

import java.text.ParseException;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidatorContext;
import org.apache.syncope.common.lib.types.EntityViolationType;
import org.apache.syncope.core.persistence.api.entity.Entity;
import org.apache.syncope.core.persistence.api.entity.Report;
import org.quartz.CronExpression;

public class ReportValidator extends AbstractValidator<ReportCheck, Report> {

    @Override
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public boolean isValid(final Report object, final ConstraintValidatorContext context) {
        boolean isValid = true;

        if (object.getCronExpression() != null) {
            try {
                new CronExpression(object.getCronExpression());
            } catch (ParseException e) {
                LOG.error("Invalid cron expression '" + object.getCronExpression() + "'", e);
                isValid = false;

                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        getTemplate(EntityViolationType.InvalidReport, "Invalid cron expression")).
                        addPropertyNode("cronExpression").addConstraintViolation();
            }
        }

        Set<String> reportletKeys = object.getReportlets().stream().
                map(Entity::getKey).collect(Collectors.toSet());
        if (reportletKeys.size() != object.getReportlets().size()) {
            LOG.error("Reportlet key must be unique");
            isValid = false;

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    getTemplate(EntityViolationType.InvalidReport, "Reportlet name must be unique")).
                    addPropertyNode("reportletConfs").addConstraintViolation();
        }

        return isValid;
    }
}
