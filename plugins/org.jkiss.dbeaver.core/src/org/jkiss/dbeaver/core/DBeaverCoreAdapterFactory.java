/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2018 Serge Rider (serge@jkiss.org)
 * Copyright (C) 2017-2018 Alexander Fedorov (alexander.fedorov@jkiss.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jkiss.dbeaver.core;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.struct.DBSObject;
import org.jkiss.dbeaver.runtime.ide.core.WorkspaceResourceResolver;

//FIXME: AF: we can rework this after "org.jkiss.dbeaver.core" bundle will be split for parts
public class DBeaverCoreAdapterFactory implements IAdapterFactory {

    private static final Class<?>[] CLASSES = new Class[] { WorkspaceResourceResolver.class };
    
    private final WorkspaceResourceResolver workspaceResourceResolver = new WorkspaceResourceResolver() {

        @Override
        public IResource resolveResource(DBSObject databaseObject) {
            if (databaseObject != null) {
                DBPDataSource dataSource = databaseObject.getDataSource();
                if (dataSource != null) {
                    DBPDataSourceContainer container = dataSource.getContainer();
                    if (container != null) {
                        return container.getRegistry().getProject();
                    }
                }
            }
            // FIXME:AF: for now it looks like reasonable default
            return DBeaverCore.getInstance().getProjectManager().getActiveProject();
        }
        
    };

    @Override
    public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
        if (adapterType == WorkspaceResourceResolver.class) {
            return adapterType.cast(workspaceResourceResolver);
        }
        return null;
    }

    @Override
    public Class<?>[] getAdapterList() {
        return CLASSES;
    }

}