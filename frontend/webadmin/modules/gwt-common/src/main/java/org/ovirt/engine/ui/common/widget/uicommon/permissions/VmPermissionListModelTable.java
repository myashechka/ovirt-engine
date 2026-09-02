package org.ovirt.engine.ui.common.widget.uicommon.permissions;

import org.ovirt.engine.core.common.businessentities.Permission;
import org.ovirt.engine.ui.common.CommonApplicationConstants;
import org.ovirt.engine.ui.common.gin.AssetProvider;
import org.ovirt.engine.ui.common.system.ClientStorage;
import org.ovirt.engine.ui.common.uicommon.model.SearchableTableModelProvider;
import org.ovirt.engine.ui.common.widget.action.PermissionActionPanelPresenterWidget;
import org.ovirt.engine.ui.common.widget.table.column.AbstractBooleanColumn;
import org.ovirt.engine.ui.uicommonweb.models.configure.PermissionListModel;

import com.google.gwt.event.shared.EventBus;

/**
 * Permissions table used for VMs only, adding a column showing whether a permission is inherited.
 */
public class VmPermissionListModelTable<E, P extends PermissionListModel<E>> extends PermissionWithInheritedPermissionListModelTable<E, P> {

    private static final CommonApplicationConstants constants = AssetProvider.getConstants();

    public VmPermissionListModelTable(
            SearchableTableModelProvider<Permission, P> modelProvider,
            EventBus eventBus, PermissionActionPanelPresenterWidget<?, ?, P> actionPanel, ClientStorage clientStorage) {
        super(modelProvider, eventBus, actionPanel, clientStorage);
    }

    @Override
    public void initTable() {
        super.initTable();

        AbstractBooleanColumn<Permission> inheritedColumn =
                new AbstractBooleanColumn<Permission>(constants.inheritedPermissionColumn()) {
                    @Override
                    public Boolean getRawValue(Permission object) {
                        return object.isInherited();
                    }
                };
        inheritedColumn.makeSortable();
        getTable().addColumn(inheritedColumn, constants.inheritedPermissionColumn(), "100px"); //$NON-NLS-1$
    }

}
