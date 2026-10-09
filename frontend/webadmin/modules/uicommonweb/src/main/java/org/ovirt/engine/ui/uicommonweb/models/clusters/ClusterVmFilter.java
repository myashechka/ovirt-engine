package org.ovirt.engine.ui.uicommonweb.models.clusters;

import org.ovirt.engine.ui.uicommonweb.ViewFilter;
import org.ovirt.engine.ui.uicompat.ConstantsManager;

public enum ClusterVmFilter implements ViewFilter<ClusterVmFilter> {

    ALL_VMS(ConstantsManager.getInstance().getConstants().allVms()) {
    },

    RUNNING_VMS(ConstantsManager.getInstance().getConstants().runningVms()) {
    };

    private String text;

    ClusterVmFilter(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public ClusterVmFilter getValue() {
        return this;
    }

}
