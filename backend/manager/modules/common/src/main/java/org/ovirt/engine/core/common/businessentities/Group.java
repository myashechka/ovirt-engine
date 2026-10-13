package org.ovirt.engine.core.common.businessentities;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.ovirt.engine.core.common.utils.ValidationUtils;
import org.ovirt.engine.core.compat.Guid;

/**
 * A named group that can be assigned to a VM or a VM pool. Assigning a group to
 * a pool assigns it to every VM in that pool, and keeps it in sync when the
 * pool's group is changed later on.
 */
public class Group implements Serializable, BusinessEntity<Guid>, Nameable, Queryable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private Guid id;

    @Size(min = 1, max = BusinessEntitiesDefinitions.GENERAL_NAME_SIZE, message = "ACTION_TYPE_FAILED_NAME_LENGTH_IS_TOO_LONG")
    @Pattern(regexp = ValidationUtils.NO_SPECIAL_CHARACTERS_I18N, message = "ACTION_TYPE_FAILED_NAME_MAY_NOT_CONTAIN_SPECIAL_CHARS")
    private String name;

    private String description;

    public Group() {
    }

    public Group(Guid id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public Object getQueryableId() {
        return id;
    }

    @Override
    public Guid getId() {
        return id;
    }

    @Override
    public void setId(Guid id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group)) {
            return false;
        }
        Group other = (Group) o;
        return Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(description, other.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return name;
    }
}
