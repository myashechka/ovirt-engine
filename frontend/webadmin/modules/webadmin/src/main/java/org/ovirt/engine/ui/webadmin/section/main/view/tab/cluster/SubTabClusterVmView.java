package org.ovirt.engine.ui.webadmin.section.main.view.tab.cluster;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.gwtbootstrap3.client.ui.constants.Styles;
import org.ovirt.engine.core.common.businessentities.Cluster;
import org.ovirt.engine.core.common.businessentities.VM;
import org.ovirt.engine.core.common.businessentities.VMStatus;
import org.ovirt.engine.ui.common.idhandler.ElementIdHandler;
import org.ovirt.engine.ui.common.presenter.FragmentParams;
import org.ovirt.engine.ui.common.uicommon.model.SearchableDetailModelProvider;
import org.ovirt.engine.ui.common.view.ViewRadioGroup;
import org.ovirt.engine.ui.common.widget.table.column.AbstractEnumColumn;
import org.ovirt.engine.ui.common.widget.table.column.AbstractLinkColumn;
import org.ovirt.engine.ui.common.widget.table.column.AbstractTextColumn;
import org.ovirt.engine.ui.uicommonweb.models.clusters.ClusterListModel;
import org.ovirt.engine.ui.uicommonweb.models.clusters.ClusterVmFilter;
import org.ovirt.engine.ui.uicommonweb.models.clusters.ClusterVmListModel;
import org.ovirt.engine.ui.uicommonweb.place.WebAdminApplicationPlaces;
import org.ovirt.engine.ui.webadmin.ApplicationConstants;
import org.ovirt.engine.ui.webadmin.gin.AssetProvider;
import org.ovirt.engine.ui.webadmin.section.main.presenter.tab.cluster.SubTabClusterVmPresenter;
import org.ovirt.engine.ui.webadmin.section.main.view.AbstractSubTabTableView;
import org.ovirt.engine.ui.webadmin.widget.table.column.AbstractUptimeColumn;
import org.ovirt.engine.ui.webadmin.widget.table.column.ColumnResizeTableLineChartProgressBar;
import org.ovirt.engine.ui.webadmin.widget.table.column.VmStatusIconColumn;
import org.ovirt.engine.ui.webadmin.widget.table.column.VmTypeColumn;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class SubTabClusterVmView extends AbstractSubTabTableView<Cluster, VM, ClusterListModel<Void>, ClusterVmListModel>
        implements SubTabClusterVmPresenter.ViewDef {

    interface ViewIdHandler extends ElementIdHandler<SubTabClusterVmView> {
        ViewIdHandler idHandler = GWT.create(ViewIdHandler.class);
    }

    private static final ApplicationConstants constants = AssetProvider.getConstants();

    @Inject
    public SubTabClusterVmView(SearchableDetailModelProvider<VM, ClusterListModel<Void>, ClusterVmListModel> modelProvider) {
        super(modelProvider);
        initTable();
        getTable().setTableOverhead(createOverheadPanel());
        initWidget(getTable());
    }

    @Override
    protected void generateIds() {
        ViewIdHandler.idHandler.generateAndSetIds(this);
    }

    private FlowPanel createOverheadPanel() {
        Label label = new Label();
        label.setText(constants.vmFilter() + ":"); //$NON-NLS-1$
        label.addStyleName(Styles.PULL_LEFT);
        label.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        label.getElement().getStyle().setMarginRight(5, Style.Unit.PX);

        ViewRadioGroup<ClusterVmFilter> viewRadioGroup = new ViewRadioGroup<>(Arrays.asList(ClusterVmFilter.values()));
        viewRadioGroup.setSelectedValue(ClusterVmFilter.ALL_VMS);
        viewRadioGroup.addChangeHandler(this::onFilterChange);

        FlowPanel overheadPanel = new FlowPanel();
        overheadPanel.add(label);
        overheadPanel.add(viewRadioGroup);

        return overheadPanel;
    }

    private void onFilterChange(ClusterVmFilter selected) {
        getDetailModel().setRunningOnly(selected == ClusterVmFilter.RUNNING_VMS);
    }

    void initTable() {
        getTable().enableColumnResizing();

        VmStatusIconColumn<VM> statusIconColumn = new VmStatusIconColumn<>();
        statusIconColumn.setContextMenuTitle(constants.statusIconVm());
        statusIconColumn.makeSortable();
        getTable().addColumn(statusIconColumn, constants.empty(), "30px"); //$NON-NLS-1$

        AbstractTextColumn<VM> nameColumn = new AbstractLinkColumn<VM>(new FieldUpdater<VM, String>() {
            @Override
            public void update(int index, VM vm, String value) {
                Map<String, String> parameters = new HashMap<>();
                parameters.put(FragmentParams.NAME.getName(), vm.getName());
                getPlaceTransitionHandler().handlePlaceTransition(
                        WebAdminApplicationPlaces.virtualMachineGeneralSubTabPlace, parameters);
            }
        }) {
            @Override
            public String getValue(VM object) {
                return object.getName();
            }
        };

        nameColumn.makeSortable();
        getTable().addColumn(nameColumn, constants.nameVm(), "220px"); //$NON-NLS-1$

        VmTypeColumn typeColumn = new VmTypeColumn();
        typeColumn.setContextMenuTitle(constants.typeVm());
        typeColumn.makeSortable(Comparator.comparing(VM::getVmType));
        getTable().addColumn(typeColumn, constants.empty(), "60px"); //$NON-NLS-1$

        AbstractTextColumn<VM> statusColumn = new AbstractEnumColumn<VM, VMStatus>() {
            @Override
            protected VMStatus getRawValue(VM object) {
                return object.getStatus();
            }
        };
        statusColumn.makeSortable();
        getTable().addColumn(statusColumn, constants.statusVm(), "220px"); //$NON-NLS-1$

        AbstractTextColumn<VM> uptimeColumn = new AbstractUptimeColumn<VM>() {
            @Override
            protected Double getRawValue(VM object) {
                return object.getElapsedTime();
            }
        };
        uptimeColumn.makeSortable();
        getTable().addColumn(uptimeColumn, constants.uptimeVm(), "220px"); //$NON-NLS-1$

        getTable().addColumn(new ColumnResizeTableLineChartProgressBar<Cluster, VM>(
                getTable(),
                Comparator.comparing((VM vm) -> lastUsageValue(vm.getCpuUsageHistory()))) {
            @Override
            protected List<Integer> getProgressValues(VM object) {
                return object.getCpuUsageHistory();
            }
        }, constants.cpuVm(), "80px"); //$NON-NLS-1$

        getTable().addColumn(new ColumnResizeTableLineChartProgressBar<Cluster, VM>(
                getTable(),
                Comparator.comparing((VM vm) -> lastUsageValue(vm.getMemoryUsageHistory()))) {
            @Override
            protected List<Integer> getProgressValues(VM object) {
                return object.getMemoryUsageHistory();
            }
        }, constants.memoryVm(), "80px"); //$NON-NLS-1$

        getTable().addColumn(new ColumnResizeTableLineChartProgressBar<Cluster, VM>(
                getTable(),
                Comparator.comparing((VM vm) -> lastUsageValue(vm.getNetworkUsageHistory()))) {
            @Override
            protected List<Integer> getProgressValues(VM object) {
                return object.getNetworkUsageHistory();
            }
        }, constants.networkVm(), "80px"); //$NON-NLS-1$

        AbstractTextColumn<VM> ipColumn = new AbstractTextColumn<VM>() {
            @Override
            public String getValue(VM object) {
                return object.getIp();
            }
        };
        ipColumn.makeSortable();
        getTable().addColumn(ipColumn, constants.ipVm(), "120px"); //$NON-NLS-1$
    }

    private static int lastUsageValue(List<Integer> history) {
        return history == null || history.isEmpty() ? 0 : history.get(history.size() - 1);
    }
}
