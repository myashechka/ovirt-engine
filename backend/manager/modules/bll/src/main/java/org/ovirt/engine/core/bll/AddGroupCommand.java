package org.ovirt.engine.core.bll;

import org.ovirt.engine.core.bll.context.CommandContext;
import org.ovirt.engine.core.common.AuditLogType;
import org.ovirt.engine.core.common.action.GroupActionParameters;
import org.ovirt.engine.core.common.businessentities.Group;
import org.ovirt.engine.core.common.errors.EngineMessage;

public class AddGroupCommand<T extends GroupActionParameters> extends GroupCommandBase<T> {

    public AddGroupCommand(T parameters, CommandContext cmdContext) {
        super(parameters, cmdContext);
    }

    @Override
    protected void executeCommand() {
        Group group = getParameters().getGroup();
        groupDao.save(group);
        setActionReturnValue(group.getId());
        setSucceeded(true);
    }

    @Override
    protected boolean validate() {
        Group group = getParameters().getGroup();

        if (group.getName() == null || group.getName().isEmpty()) {
            addValidationMessage(EngineMessage.GROUP_BAD_NAME);
            return false;
        }

        if (groupDao.getAll().stream().anyMatch(g -> g.getName().equals(group.getName()))) {
            addValidationMessage(EngineMessage.GROUP_NAME_ALREADY_EXISTS);
            return false;
        }

        return true;
    }

    @Override
    public AuditLogType getAuditLogTypeValue() {
        return getSucceeded() ? AuditLogType.USER_ADDED_GROUP : AuditLogType.USER_ADDED_GROUP_FAILED;
    }

    @Override
    public void init() {
        addCustomValue("groupName", getParameters().getGroup().getName());
    }
}
