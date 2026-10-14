package org.ovirt.engine.api.restapi.resource;

import java.util.List;

import javax.ws.rs.core.Response;

import org.ovirt.engine.api.model.VmGroup;
import org.ovirt.engine.api.model.VmGroups;
import org.ovirt.engine.api.resource.VmGroupResource;
import org.ovirt.engine.api.resource.VmGroupsResource;
import org.ovirt.engine.core.common.businessentities.Group;
import org.ovirt.engine.core.common.queries.QueryParametersBase;
import org.ovirt.engine.core.common.queries.QueryType;

public class BackendVmGroupsResource extends AbstractBackendVmGroupsResource implements VmGroupsResource {

    @Override
    public VmGroups list() {
        return performList();
    }

    @Override
    protected List<Group> getVmGroupsCollection() {
        return getBackendCollection(QueryType.GetAllGroups, new QueryParametersBase());
    }

    @Override
    public Response add(VmGroup group) {
        return super.add(group);
    }

    @Override
    public VmGroupResource getGroupResource(String id) {
        return inject(new BackendVmGroupResource(id));
    }
}
