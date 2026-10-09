package org.ovirt.engine.core.bll;

import java.util.List;

import javax.inject.Inject;

import org.ovirt.engine.core.bll.context.EngineContext;
import org.ovirt.engine.core.common.businessentities.VM;
import org.ovirt.engine.core.common.queries.GetVmsByClusterIdParameters;
import org.ovirt.engine.core.dao.VmDao;

public class GetVmsByClusterIdQuery<P extends GetVmsByClusterIdParameters> extends QueriesCommandBase<P> {
    @Inject
    private VmDao vmDao;

    public GetVmsByClusterIdQuery(P parameters, EngineContext engineContext) {
        super(parameters, engineContext);
    }

    @Override
    protected void executeQueryCommand() {
        List<VM> vms;
        if (getParameters().isRunningOnly()) {
            vms = vmDao.getAllRunningForCluster(getParameters().getId(), getUserID(), getParameters().isFiltered());
        } else {
            vms = vmDao.getAllForCluster(getParameters().getId(), getUserID(), getParameters().isFiltered());
        }
        getQueryReturnValue().setReturnValue(vms);
    }
}
