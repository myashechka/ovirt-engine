package org.ovirt.engine.core.bll;

import org.ovirt.engine.core.bll.context.CommandContext;
import org.ovirt.engine.core.common.AuditLogType;
import org.ovirt.engine.core.common.action.GroupActionParametersBase;
import org.ovirt.engine.core.common.errors.EngineMessage;

public class RemoveGroupCommand<T extends GroupActionParametersBase> extends GroupCommandBase<T> {

    public RemoveGroupCommand(T parameters, CommandContext cmdContext) {
        super(parameters, cmdContext);
    }

    @Override
    protected void executeCommand() {
        groupDao.remove(getGroupId());
        setSucceeded(true);
    }

    @Override
    protected boolean validate() {
        if (getGroupId() == null || getGroup() == null) {
            addValidationMessage(EngineMessage.GROUP_NOT_EXISTS);
            return false;
        }
        return true;
    }

    @Override
    public AuditLogType getAuditLogTypeValue() {
        return getSucceeded() ? AuditLogType.USER_REMOVED_GROUP : AuditLogType.USER_REMOVED_GROUP_FAILED;
    }
}
