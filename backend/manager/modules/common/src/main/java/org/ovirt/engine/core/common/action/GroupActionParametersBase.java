package org.ovirt.engine.core.common.action;

import org.ovirt.engine.core.compat.Guid;

public class GroupActionParametersBase extends ActionParametersBase {
    private static final long serialVersionUID = 1L;

    private Guid groupId;

    public GroupActionParametersBase() {
    }

    public GroupActionParametersBase(Guid groupId) {
        this.groupId = groupId;
    }

    public Guid getGroupId() {
        return groupId;
    }
}
