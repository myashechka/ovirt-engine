package org.ovirt.engine.ui.webadmin.section.main.view.tab.datacenter;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.ovirt.engine.core.common.businessentities.StoragePool;
import org.ovirt.engine.core.common.businessentities.VmPool;
import org.ovirt.engine.core.common.businessentities.VmPoolType;
import org.ovirt.engine.ui.common.idhandler.ElementIdHandler;
import org.ovirt.engine.ui.common.presenter.FragmentParams;
import org.ovirt.engine.ui.common.uicommon.model.SearchableDetailModelProvider;
import org.ovirt.engine.ui.common.widget.table.column.AbstractEnumColumn;
import org.ovirt.engine.ui.common.widget.table.column.AbstractLinkColumn;
import org.ovirt.engine.ui.common.widget.table.column.AbstractTextColumn;
import org.ovirt.engine.ui.uicommonweb.models.datacenters.DataCenterListModel;
import org.ovirt.engine.ui.uicommonweb.models.datacenters.DataCenterVmPoolListModel;
import org.ovirt.engine.ui.uicommonweb.place.WebAdminApplicationPlaces;
import org.ovirt.engine.ui.webadmin.ApplicationConstants;
import org.ovirt.engine.ui.webadmin.gin.AssetProvider;
import org.ovirt.engine.ui.webadmin.section.main.presenter.tab.datacenter.SubTabDataCenterVmPoolPresenter;
import org.ovirt.engine.ui.webadmin.section.main.view.AbstractSubTabTableView;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;

public class SubTabDataCenterVmPoolView extends AbstractSubTabTableView<StoragePool, VmPool, DataCenterListModel, DataCenterVmPoolListModel>
        implements SubTabDataCenterVmPoolPresenter.ViewDef {

    interface ViewIdHandler extends ElementIdHandler<SubTabDataCenterVmPoolView> {
        ViewIdHandler idHandler = GWT.create(ViewIdHandler.class);
    }

    private static final ApplicationConstants constants = AssetProvider.getConstants();

    @Inject
    public SubTabDataCenterVmPoolView(SearchableDetailModelProvider<VmPool, DataCenterListModel, DataCenterVmPoolListModel> modelProvider) {
        super(modelProvider);
        initTable();
        initWidget(getTable());
    }

    @Override
    protected void generateIds() {
        ViewIdHandler.idHandler.generateAndSetIds(this);
    }

    void initTable() {
        getTable().enableColumnResizing();

        AbstractTextColumn<VmPool> nameColumn = new AbstractLinkColumn<VmPool>(new FieldUpdater<VmPool, String>() {
            @Override
            public void update(int index, VmPool pool, String value) {
                Map<String, String> parameters = new HashMap<>();
                parameters.put(FragmentParams.NAME.getName(), pool.getName());
                getPlaceTransitionHandler().handlePlaceTransition(
                        WebAdminApplicationPlaces.poolGeneralSubTabPlace, parameters);
            }
        }) {
            @Override
            public String getValue(VmPool object) {
                return object.getName();
            }
        };
        nameColumn.makeSortable();
        getTable().addColumn(nameColumn, constants.namePool(), "200px"); //$NON-NLS-1$

        AbstractTextColumn<VmPool> typeColumn = new AbstractEnumColumn<VmPool, VmPoolType>() {
            @Override
            public VmPoolType getRawValue(VmPool object) {
                return object.getVmPoolType();
            }
        };
        typeColumn.makeSortable();
        getTable().addColumn(typeColumn, constants.typePool(), "150px"); //$NON-NLS-1$

        AbstractTextColumn<VmPool> clusterColumn = new AbstractTextColumn<VmPool>() {
            @Override
            public String getValue(VmPool object) {
                return object.getClusterName();
            }
        };
        clusterColumn.makeSortable();
        getTable().addColumn(clusterColumn, constants.clusterPool(), "150px"); //$NON-NLS-1$

        AbstractTextColumn<VmPool> groupColumn = new AbstractTextColumn<VmPool>() {
            @Override
            public String getValue(VmPool object) {
                return object.getGroupName();
            }
        };
        getTable().addColumn(groupColumn, constants.groupLabel(), "150px"); //$NON-NLS-1$

        AbstractTextColumn<VmPool> descColumn = new AbstractTextColumn<VmPool>() {
            @Override
            public String getValue(VmPool object) {
                return object.getVmPoolDescription();
            }
        };
        descColumn.makeSortable();
        getTable().addColumn(descColumn, constants.descriptionPool(), "300px"); //$NON-NLS-1$
    }

}
