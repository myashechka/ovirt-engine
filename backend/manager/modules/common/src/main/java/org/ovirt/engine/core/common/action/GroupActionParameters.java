package org.ovirt.engine.core.common.action;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.ovirt.engine.core.common.businessentities.Group;

public class GroupActionParameters extends GroupActionParametersBase {
    private static final long serialVersionUID = 1L;

    @Valid
    @NotNull
    private Group group;

    private GroupActionParameters() {
    }

    public GroupActionParameters(@NotNull Group group) {
        super(group.getId());
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }
}
