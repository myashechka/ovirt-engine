package org.ovirt.engine.core.bll;

import javax.inject.Inject;

import org.ovirt.engine.core.bll.context.EngineContext;
import org.ovirt.engine.core.common.queries.NameQueryParameters;
import org.ovirt.engine.core.dao.GroupDao;

public class GetGroupByNameQuery<P extends NameQueryParameters> extends QueriesCommandBase<P> {
    @Inject
    GroupDao groupDao;

    public GetGroupByNameQuery(P parameters, EngineContext engineContext) {
        super(parameters, engineContext);
    }

    @Override
    protected void executeQueryCommand() {
        getQueryReturnValue().setReturnValue(groupDao.getByName(getParameters().getName()));
    }
}
