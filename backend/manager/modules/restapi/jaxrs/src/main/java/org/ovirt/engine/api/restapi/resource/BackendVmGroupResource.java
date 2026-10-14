package org.ovirt.engine.api.restapi.resource;

import org.ovirt.engine.api.model.VmGroup;
import org.ovirt.engine.api.resource.VmGroupResource;
import org.ovirt.engine.core.common.action.ActionParametersBase;
import org.ovirt.engine.core.common.action.ActionType;
import org.ovirt.engine.core.common.action.GroupActionParameters;
import org.ovirt.engine.core.common.businessentities.Group;
import org.ovirt.engine.core.common.queries.IdQueryParameters;
import org.ovirt.engine.core.common.queries.QueryType;

public class BackendVmGroupResource extends AbstractBackendVmGroupResource implements VmGroupResource {

    protected BackendVmGroupResource(String id) {
        super(id);
    }

    @Override
    public VmGroup get() {
        return super.get();
    }

    @Override
    public VmGroup update(VmGroup resource) {
        return performUpdate(resource,
                new QueryIdResolver<>(QueryType.GetGroupById, IdQueryParameters.class),
                ActionType.UpdateGroup,
                new UpdateParametersProvider());
    }

    protected class UpdateParametersProvider implements ParametersProvider<VmGroup, Group> {
        @Override
        public ActionParametersBase getParameters(VmGroup incoming, Group entity) {
            Group map = map(incoming, entity);
            return new GroupActionParameters(map);
        }
    }
}
