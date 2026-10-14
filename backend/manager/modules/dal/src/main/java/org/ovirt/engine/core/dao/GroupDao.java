package org.ovirt.engine.core.dao;

import java.util.List;

import org.ovirt.engine.core.common.businessentities.Group;
import org.ovirt.engine.core.compat.Guid;

/**
 * {@code GroupDao} defines a type for performing CRUD operations on instances of {@link Group}.
 */
public interface GroupDao extends Dao {

    /**
     * Retrieves the group with the specified id.
     */
    Group get(Guid id);

    /**
     * Retrieves all groups.
     */
    List<Group> getAll();

    /**
     * Retrieves the group with the specified name.
     */
    Group getByName(String name);

    /**
     * Saves the supplied group.
     */
    void save(Group group);

    /**
     * Updates the supplied group.
     */
    void update(Group group);

    /**
     * Removes the group with the specified id.
     */
    void remove(Guid id);
}
