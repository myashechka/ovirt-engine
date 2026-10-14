package org.ovirt.engine.api.restapi.resource;

import java.util.List;

import javax.ws.rs.core.Response;

import org.ovirt.engine.api.model.VmGroup;
import org.ovirt.engine.api.model.VmGroups;
import org.ovirt.engine.core.common.action.ActionType;
import org.ovirt.engine.core.common.action.GroupActionParameters;
import org.ovirt.engine.core.common.businessentities.Group;
import org.ovirt.engine.core.common.queries.IdQueryParameters;
import org.ovirt.engine.core.common.queries.QueryType;
import org.ovirt.engine.core.compat.Guid;

public abstract class AbstractBackendVmGroupsResource
        extends AbstractBackendCollectionResource<VmGroup, Group> {

    public AbstractBackendVmGroupsResource() {
        super(VmGroup.class, Group.class);
    }

    protected VmGroups mapCollection(List<Group> entities) {
        VmGroups collection = new VmGroups();
        for (Group entity : entities) {
            collection.getVmGroups().add(addLinks(populate(map(entity), entity)));
        }
        return collection;
    }

    protected Response add(VmGroup group) {
        validateParameters(group, "name");
        Group entity = map(group);
        return performCreate(ActionType.AddGroup,
                new GroupActionParameters(entity),
                new QueryIdResolver<Guid>(QueryType.GetGroupById, IdQueryParameters.class));
    }

    protected abstract List<Group> getVmGroupsCollection();

    protected VmGroups performList() {
        return mapCollection(getVmGroupsCollection());
    }
}
