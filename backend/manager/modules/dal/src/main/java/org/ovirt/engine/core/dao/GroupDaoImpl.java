package org.ovirt.engine.core.dao;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.ovirt.engine.core.common.businessentities.Group;
import org.ovirt.engine.core.compat.Guid;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@Named
@Singleton
public class GroupDaoImpl extends BaseDao implements GroupDao {
    private static final RowMapper<Group> groupRowMapper = (rs, rowNum) -> {
        Group entity = new Group();
        entity.setId(getGuidDefaultNewGuid(rs, "group_id"));
        entity.setName(rs.getString("group_name"));
        entity.setDescription(rs.getString("group_description"));
        return entity;
    };

    @Override
    public Group get(Guid id) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("group_id", id);

        return getCallsHandler()
                .executeRead("GetGroupById", groupRowMapper, parameterSource);
    }

    @Override
    public Group getByName(String name) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("group_name", name);

        return getCallsHandler()
                .executeRead("GetGroupByName", groupRowMapper, parameterSource);
    }

    @Override
    public List<Group> getAll() {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource();

        return getCallsHandler()
                .executeReadList("GetAllGroups", groupRowMapper, parameterSource);
    }

    @Override
    public void save(Group group) {
        if (Guid.isNullOrEmpty(group.getId())) {
            group.setId(Guid.newGuid());
        }

        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("group_id", group.getId())
                .addValue("group_name", group.getName())
                .addValue("group_description", group.getDescription());

        getCallsHandler().executeModification("InsertGroup", parameterSource);
    }

    @Override
    public void update(Group group) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("group_id", group.getId())
                .addValue("group_name", group.getName())
                .addValue("group_description", group.getDescription());

        getCallsHandler().executeModification("UpdateGroup", parameterSource);
    }

    @Override
    public void remove(Guid id) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("group_id", id);

        getCallsHandler().executeModification("DeleteGroup", parameterSource);
    }
}
