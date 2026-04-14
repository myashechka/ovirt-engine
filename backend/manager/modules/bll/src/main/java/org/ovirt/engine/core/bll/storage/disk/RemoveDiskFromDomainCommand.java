package org.ovirt.engine.core.bll.storage.disk;

import java.util.Date;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Typed;
import javax.inject.Inject;

import org.ovirt.engine.core.bll.InternalCommandAttribute;
import org.ovirt.engine.core.bll.NonTransactiveCommandAttribute;
import org.ovirt.engine.core.bll.SerialChildCommandsExecutionCallback;
import org.ovirt.engine.core.bll.SerialChildExecutingCommand;
import org.ovirt.engine.core.bll.context.CommandContext;
import org.ovirt.engine.core.bll.storage.disk.image.BaseImagesCommand;
import org.ovirt.engine.core.bll.tasks.interfaces.CommandCallback;
import org.ovirt.engine.core.common.action.ActionReturnValue;
import org.ovirt.engine.core.common.action.ActionType;
import org.ovirt.engine.core.common.action.RemoveImageParameters;
import org.ovirt.engine.core.common.asynctasks.AsyncTaskType;
import org.ovirt.engine.core.common.job.JobExecutionStatus;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.core.dao.DiskDao;
import org.ovirt.engine.core.dao.ImageDao;
import org.ovirt.engine.core.dao.StorageDomainDao;

/**
 * This command clears the source/target storage domain when performing disk migration.
 */
@InternalCommandAttribute
@NonTransactiveCommandAttribute(forceCompensation=true)
public class RemoveDiskFromDomainCommand<T extends RemoveImageParameters> extends BaseImagesCommand<T> implements SerialChildExecutingCommand {
    @Inject
    private StorageDomainDao storageDomainDao;
    @Inject
    private DiskDao diskDao;
    @Inject
    private ImageDao imageDao;

    @Inject
    @Typed(SerialChildCommandsExecutionCallback.class)
    private Instance<SerialChildCommandsExecutionCallback> callbackProvider;

    @Override
    public CommandCallback getCallback() {
        return callbackProvider.get();
    }

    public RemoveDiskFromDomainCommand(T parameters, CommandContext commandContext) {
        super(parameters, commandContext);
    }

    public RemoveDiskFromDomainCommand(Guid commandId) {
        super(commandId);
    }

    @Override
    protected void executeCommand() {
        persistCommand(getParameters().getParentCommand(), getCallback() != null);
        RemoveImageParameters parameters = getParameters();
        parameters.setParentCommand(getActionType());
        parameters.setParentParameters(getParameters());

        ActionReturnValue returnValue = runInternalActionWithTasksContext(ActionType.RemoveImage, parameters);
        if (!returnValue.getSucceeded()) {
            endWithFailure();
        }
        setSucceeded(returnValue.getSucceeded());
    }

    @Override
    protected AsyncTaskType getTaskType() {
        return AsyncTaskType.deleteImage;
    }

    @Override
    public boolean performNextOperation(int completedChildCount) {
        return false;
    }

    @Override
    protected void endWithFailure() {
        // Since an unsuccessful cleanup of the storage domain is not a critical error when performing disk migration,
        // the job status is marked as "ABORTED" so that the task will later receive a warning status, not an error.
        getExecutionContext().getJob().setStatus(JobExecutionStatus.ABORTED);
        getExecutionContext().getJob().setEndTime(new Date());
        super.endWithFailure();
    }

    @Override
    public Map<String, String> getJobMessageProperties() {
        if (jobProperties == null) {
            jobProperties = super.getJobMessageProperties();

            Guid diskId = imageDao.get(getParameters().getImageId()).getDiskId();
            jobProperties.put("diskalias", diskDao.get(diskId).getDiskAlias());
            jobProperties.put("storagedomain", storageDomainDao.get(getParameters().getStorageDomainId()).getStorageName());
        }
        return jobProperties;
    }
}
