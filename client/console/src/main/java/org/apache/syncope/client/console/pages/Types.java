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
package org.apache.syncope.client.console.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.AjaxBootstrapTabbedPanel;
import java.util.ArrayList;
import java.util.List;
import org.apache.syncope.client.console.panels.AnyTypeClassesPanel;
import org.apache.syncope.client.console.panels.AnyTypePanel;
import org.apache.syncope.client.console.panels.RelationshipTypePanel;
import org.apache.syncope.client.console.panels.SchemasPanel;
import org.apache.syncope.client.console.wicket.markup.html.bootstrap.dialog.BaseModal;
import org.apache.syncope.common.lib.to.AbstractSchemaTO;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.syncope.common.lib.to.AnyTypeClassTO;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

public class Types extends BasePage {

    private static final long serialVersionUID = 8091922398776299403L;

    private final BaseModal<AbstractSchemaTO> schemaModal;

    private final BaseModal<AnyTypeClassTO> anyTypeClassModal;

    private final AjaxBootstrapTabbedPanel<ITab> tabbedPanel;

    private enum Type {
        SCHEMA,
        ANYTYPECLASS,
        ANYTYPE,
        RELATIONSHIPTYPE;

    }

    public Types(final PageParameters parameters) {
        super(parameters);

        this.schemaModal = new BaseModal<>("schemaModal");
        this.anyTypeClassModal = new BaseModal<>("anyTypeClassModal");

        final WebMarkupContainer content = new WebMarkupContainer("content");
        content.add(new Label("header", "Types"));
        content.setOutputMarkupId(true);
        tabbedPanel = new AjaxBootstrapTabbedPanel<>("tabbedPanel", buildTabList());
        content.add(tabbedPanel);

        add(content);
        addWindowWindowClosedCallback(schemaModal);
        addWindowWindowClosedCallback(anyTypeClassModal);
        add(schemaModal);
        add(anyTypeClassModal);
    }

    private List<ITab> buildTabList() {

        final List<ITab> tabs = new ArrayList<>();

        tabs.add(new AbstractTab(new Model<>("RelationshipType")) {

            private static final long serialVersionUID = -6815067322125799251L;

            @Override
            public Panel getPanel(final String panelId) {
                return new RelationshipTypePanel(panelId, getPageReference());
            }
        });

        tabs.add(new AbstractTab(new Model<>("AnyTypes")) {

            private static final long serialVersionUID = -6815067322125799251L;

            @Override
            public Panel getPanel(final String panelId) {
                return new AnyTypePanel(panelId, getPageReference());
            }
        });

        tabs.add(new AbstractTab(new Model<>("AnyTypeClasses")) {

            private static final long serialVersionUID = -6815067322125799251L;

            @Override
            public Panel getPanel(final String panelId) {
                return new AnyTypeClassesPanel(panelId, getPageReference(), anyTypeClassModal);
            }
        });

        tabs.add(new AbstractTab(new Model<>("Schemas")) {

            private static final long serialVersionUID = -6815067322125799251L;

            @Override
            public Panel getPanel(final String panelId) {
                return new SchemasPanel(panelId, getPageReference(), schemaModal);
            }
        });

        return tabs;
    }

    private void addWindowWindowClosedCallback(final BaseModal<?> modal) {
        modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

            private static final long serialVersionUID = 8804221891699487139L;

            @Override
            public void onClose(final AjaxRequestTarget target) {
                tabbedPanel.setSelectedTab(tabbedPanel.getSelectedTab());
                target.add(tabbedPanel);
                modal.show(false);
                notificationPanel.refresh(target);
            }
        });
    }
}
