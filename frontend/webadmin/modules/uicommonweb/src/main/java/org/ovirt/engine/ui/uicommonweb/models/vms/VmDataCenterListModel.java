package org.ovirt.engine.ui.uicommonweb.models.vms;

import java.util.Collection;
import java.util.stream.Collectors;

import org.ovirt.engine.core.common.businessentities.VM;
import org.ovirt.engine.core.common.interfaces.SearchType;
import org.ovirt.engine.core.common.queries.QueryType;
import org.ovirt.engine.core.common.queries.SearchParameters;
import org.ovirt.engine.ui.uicommonweb.help.HelpTag;
import org.ovirt.engine.ui.uicommonweb.models.SearchableListModel;
import org.ovirt.engine.ui.uicompat.ConstantsManager;
import org.ovirt.engine.ui.uicompat.PropertyChangedEventArgs;

import com.google.inject.Inject;

/**
 * Lists the VMs that belong to the same Data Center as the currently selected VM
 * (this model's {@code entity}), excluding the selected VM itself.
 *
 * This is a standalone {@link SearchableListModel} (not a {@link VmListModel} subclass):
 * since it is wired as a detail model of the main VM list (itself a {@code VmListModel<Void>}),
 * extending the same generic {@code VmListModel<E>} template here would make GIN's static
 * injector generation see this model as depending on its own class hierarchy, which it
 * reports as a circular dependency. A plain {@code SearchableListModel<VM, VM>} avoids that,
 * at the cost of not supporting the VM grid's context-menu commands (Run/Stop/...) here.
 */
public class VmDataCenterListModel extends SearchableListModel<VM, VM> {

    @Inject
    public VmDataCenterListModel() {
        setTitle(ConstantsManager.getInstance().getConstants().virtualMachinesTitle());
        setHelpTag(HelpTag.virtual_machines);
        setHashName("datacenter_vms"); //$NON-NLS-1$
    }

    @Override
    protected String getListName() {
        return "VmDataCenterListModel"; //$NON-NLS-1$
    }

    @Override
    protected void onEntityChanged() {
        super.onEntityChanged();
        getSearchCommand().execute();
    }

    @Override
    public void search() {
        if (getEntity() != null && getEntity().getStoragePoolName() != null) {
            setSearchString("Vms: datacenter=" + getEntity().getStoragePoolName()); //$NON-NLS-1$
            super.search();
        }
    }

    @Override
    protected void syncSearch() {
        SearchParameters tempVar = new SearchParameters(applySortOptions(getSearchString()), SearchType.VM);
        tempVar.setRefresh(getIsQueryFirstTime());
        super.syncSearch(QueryType.Search, tempVar);
    }

    @Override
    public void setItems(Collection<VM> value) {
        Collection<VM> filtered = value == null ? null
                : value.stream()
                        .filter(vm -> getEntity() == null || !vm.getId().equals(getEntity().getId()))
                        .collect(Collectors.toList());
        super.setItems(filtered);
    }

    @Override
    protected void entityPropertyChanged(Object sender, PropertyChangedEventArgs e) {
        super.entityPropertyChanged(sender, e);

        if (e.propertyName.equals("storagePoolId")) { //$NON-NLS-1$
            getSearchCommand().execute();
        }
    }
}
