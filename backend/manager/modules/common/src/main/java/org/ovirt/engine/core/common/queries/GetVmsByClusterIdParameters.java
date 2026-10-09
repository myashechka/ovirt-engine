package org.ovirt.engine.core.common.queries;

import org.ovirt.engine.core.compat.Guid;

public class GetVmsByClusterIdParameters extends IdQueryParameters {
    private static final long serialVersionUID = 1L;

    /**
     * True to get only running VMs of the cluster. False to get all VMs of the cluster.
     */
    private boolean runningOnly;

    public GetVmsByClusterIdParameters() {
    }

    public GetVmsByClusterIdParameters(Guid clusterId) {
        super(clusterId);
    }

    public boolean isRunningOnly() {
        return runningOnly;
    }

    public void setRunningOnly(boolean runningOnly) {
        this.runningOnly = runningOnly;
    }
}
