package org.ovirt.engine.api.restapi.resource;

import javax.ws.rs.core.Response;

import org.ovirt.engine.api.model.VmGroup;
import org.ovirt.engine.core.common.action.ActionType;
import org.ovirt.engine.core.common.action.GroupActionParametersBase;
import org.ovirt.engine.core.common.businessentities.Group;
import org.ovirt.engine.core.common.queries.IdQueryParameters;
import org.ovirt.engine.core.common.queries.QueryType;

public abstract class AbstractBackendVmGroupResource
        extends AbstractBackendSubResource<VmGroup, Group> {

    protected AbstractBackendVmGroupResource(String id) {
        super(id, VmGroup.class, Group.class);
    }

    protected VmGroup get() {
        return performGet(QueryType.GetGroupById, new IdQueryParameters(guid));
    }

    public Response remove() {
        get();
        return performAction(ActionType.RemoveGroup, new GroupActionParametersBase(guid));
    }
}
