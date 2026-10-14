package org.ovirt.engine.api.restapi.types;

import org.ovirt.engine.api.model.VmGroup;
import org.ovirt.engine.api.restapi.utils.GuidUtils;

public class VmGroupMapper {
    @Mapping(from = VmGroup.class, to = org.ovirt.engine.core.common.businessentities.Group.class)
    public static org.ovirt.engine.core.common.businessentities.Group map(VmGroup model,
            org.ovirt.engine.core.common.businessentities.Group template) {
        org.ovirt.engine.core.common.businessentities.Group entity =
                template != null ? template : new org.ovirt.engine.core.common.businessentities.Group();
        if (model.isSetId()) {
            entity.setId(GuidUtils.asGuid(model.getId()));
        }
        if (model.isSetName()) {
            entity.setName(model.getName());
        }
        if (model.isSetDescription()) {
            entity.setDescription(model.getDescription());
        }
        return entity;
    }

    @Mapping(from = org.ovirt.engine.core.common.businessentities.Group.class, to = VmGroup.class)
    public static VmGroup map(org.ovirt.engine.core.common.businessentities.Group entity, VmGroup template) {
        VmGroup model = template != null ? template : new VmGroup();
        if (entity.getId() != null) {
            model.setId(entity.getId().toString());
        }
        if (entity.getName() != null) {
            model.setName(entity.getName());
        }
        if (entity.getDescription() != null) {
            model.setDescription(entity.getDescription());
        }
        return model;
    }
}
