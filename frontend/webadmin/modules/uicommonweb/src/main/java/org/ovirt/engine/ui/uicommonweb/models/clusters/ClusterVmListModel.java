package org.ovirt.engine.ui.uicommonweb.models.clusters;

import org.ovirt.engine.core.common.businessentities.Cluster;
import org.ovirt.engine.core.common.businessentities.VM;
import org.ovirt.engine.core.common.queries.GetVmsByClusterIdParameters;
import org.ovirt.engine.core.common.queries.QueryType;
import org.ovirt.engine.ui.uicommonweb.models.ConfirmationModelSettingsManager;
import org.ovirt.engine.ui.uicommonweb.models.VmErrataCountModel;
import org.ovirt.engine.ui.uicommonweb.models.configure.PermissionListModel;
import org.ovirt.engine.ui.uicommonweb.models.configure.labels.list.VmAffinityLabelListModel;
import org.ovirt.engine.ui.uicommonweb.models.configure.scheduling.affinity_groups.list.VmAffinityGroupListModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.ImportVmsModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.VmAppListModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.VmDevicesListModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.VmDiskListModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.VmEventListModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.VmGeneralModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.VmGuestContainerListModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.VmGuestInfoModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.VmInterfaceListModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.VmListModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.VmSnapshotListModel;
import org.ovirt.engine.ui.uicommonweb.models.vms.hostdev.VmHostDeviceListModel;
import org.ovirt.engine.ui.uicompat.PropertyChangedEventArgs;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ClusterVmListModel extends VmListModel<Cluster> {

    private boolean runningOnly;

    @Inject
    public ClusterVmListModel(final VmGeneralModel vmGeneralModel,
            final VmInterfaceListModel vmInterfaceListModel,
            final VmDiskListModel vmDiskListModel,
            final VmSnapshotListModel vmSnapshotListModel,
            final VmEventListModel vmEventListModel,
            final VmAppListModel<VM> vmAppListModel,
            final PermissionListModel<VM> permissionListModel,
            final VmAffinityGroupListModel vmAffinityGroupListModel,
            final VmGuestInfoModel vmGuestInfoModel,
            final Provider<ImportVmsModel> importVmsModelProvider,
            final VmHostDeviceListModel vmHostDeviceListModel,
            final VmDevicesListModel<VM> vmDevicesListModel,
            final VmAffinityLabelListModel vmAffinityLabelListModel,
            final VmErrataCountModel vmErrataCountModel,
            final VmGuestContainerListModel vmGuestContainerListModel,
            final ConfirmationModelSettingsManager confirmationModelSettingsManager) {
        super(vmGeneralModel, vmInterfaceListModel, vmDiskListModel,
                vmSnapshotListModel, vmEventListModel, vmAppListModel,
                permissionListModel, vmAffinityGroupListModel, vmGuestInfoModel,
                importVmsModelProvider, vmHostDeviceListModel, vmDevicesListModel,
                vmAffinityLabelListModel, vmErrataCountModel, vmGuestContainerListModel,
                confirmationModelSettingsManager);
        getSearchNextPageCommand().setIsAvailable(false);
        getSearchPreviousPageCommand().setIsAvailable(false);
    }

    @Override
    protected void onEntityChanged() {
        super.onEntityChanged();
        getSearchCommand().execute();
    }

    @Override
    public void search() {
        if (getEntity() != null) {
            super.search();
        }
    }

    public void setRunningOnly(boolean runningOnly) {
        // prevent query if value is not changed since the query is refreshed on timer interval
        if (this.runningOnly != runningOnly) {
            this.runningOnly = runningOnly;
            syncSearch();
        }
    }

    @Override
    protected void syncSearch() {
        if (getEntity() == null) {
            return;
        }

        GetVmsByClusterIdParameters tempVar = new GetVmsByClusterIdParameters(getEntity().getId());
        tempVar.setRunningOnly(runningOnly);
        tempVar.setRefresh(getIsQueryFirstTime());
        super.syncSearch(QueryType.GetVmsByClusterId, tempVar);
    }

    @Override
    public boolean supportsServerSideSorting() {
        // This model fetches VMs via a dedicated DAO query, not via the generic search engine,
        // so sorting is done on the client using column comparators.
        return false;
    }

    @Override
    protected void entityPropertyChanged(Object sender, PropertyChangedEventArgs e) {
        super.entityPropertyChanged(sender, e);

        if (e.propertyName.equals("name")) { //$NON-NLS-1$
            getSearchCommand().execute();
        }
    }

}
