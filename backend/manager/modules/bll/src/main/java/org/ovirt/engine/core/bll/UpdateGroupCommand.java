package org.ovirt.engine.core.bll;

import org.ovirt.engine.core.bll.context.CommandContext;
import org.ovirt.engine.core.common.AuditLogType;
import org.ovirt.engine.core.common.action.GroupActionParameters;
import org.ovirt.engine.core.common.businessentities.Group;
import org.ovirt.engine.core.common.errors.EngineMessage;

public class UpdateGroupCommand<T extends GroupActionParameters> extends GroupCommandBase<T> {

    public UpdateGroupCommand(T parameters, CommandContext cmdContext) {
        super(parameters, cmdContext);
    }

    @Override
    protected void executeCommand() {
        groupDao.update(getParameters().getGroup());
        setActionReturnValue(getParameters().getGroup());
        setSucceeded(true);
    }

    @Override
    protected boolean validate() {
        if (getGroupId() == null || groupDao.get(getGroupId()) == null) {
            addValidationMessage(EngineMessage.GROUP_NOT_EXISTS);
            return false;
        }

        Group group = getParameters().getGroup();

        if (group.getName() == null || group.getName().isEmpty()) {
            addValidationMessage(EngineMessage.GROUP_BAD_NAME);
            return false;
        }

        boolean nameTaken = groupDao.getAll().stream()
                .anyMatch(g -> g.getName().equals(group.getName()) && !g.getId().equals(group.getId()));
        if (nameTaken) {
            addValidationMessage(EngineMessage.GROUP_NAME_ALREADY_EXISTS);
            return false;
        }

        return true;
    }

    @Override
    public AuditLogType getAuditLogTypeValue() {
        return getSucceeded() ? AuditLogType.USER_UPDATED_GROUP : AuditLogType.USER_UPDATED_GROUP_FAILED;
    }
}
