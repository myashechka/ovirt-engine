package org.ovirt.engine.core.bll;

import javax.inject.Inject;

import org.ovirt.engine.core.bll.context.EngineContext;
import org.ovirt.engine.core.common.queries.IdQueryParameters;
import org.ovirt.engine.core.dao.GroupDao;

public class GetGroupByIdQuery<P extends IdQueryParameters> extends QueriesCommandBase<P> {
    @Inject
    GroupDao groupDao;

    public GetGroupByIdQuery(P parameters, EngineContext engineContext) {
        super(parameters, engineContext);
    }

    @Override
    protected void executeQueryCommand() {
        getQueryReturnValue().setReturnValue(groupDao.get(getParameters().getId()));
    }
}
