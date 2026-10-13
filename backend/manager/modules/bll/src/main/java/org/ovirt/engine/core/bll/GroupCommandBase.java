package org.ovirt.engine.core.bll;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.ovirt.engine.core.bll.context.CommandContext;
import org.ovirt.engine.core.bll.utils.PermissionSubject;
import org.ovirt.engine.core.common.VdcObjectType;
import org.ovirt.engine.core.common.action.GroupActionParametersBase;
import org.ovirt.engine.core.common.businessentities.ActionGroup;
import org.ovirt.engine.core.common.businessentities.Group;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.core.dao.GroupDao;

public abstract class GroupCommandBase<T extends GroupActionParametersBase> extends CommandBase<T> {
    private Group group;

    @Inject
    protected GroupDao groupDao;

    public GroupCommandBase(T parameters, CommandContext cmdContext) {
        super(parameters, cmdContext);
    }

    protected Guid getGroupId() {
        return getParameters().getGroupId();
    }

    protected Group getGroup() {
        if (group == null && getGroupId() != null) {
            group = groupDao.get(getGroupId());
        }
        return group;
    }

    public String getGroupName() {
        return getGroup() != null ? getGroup().getName() : null;
    }

    @Override
    public List<PermissionSubject> getPermissionCheckSubjects() {
        return Collections.singletonList(new PermissionSubject(Guid.SYSTEM,
                VdcObjectType.System,
                ActionGroup.TAG_MANAGEMENT));
    }

    @Override
    public void init() {
        addCustomValue("groupName", getGroupName());
    }
}
