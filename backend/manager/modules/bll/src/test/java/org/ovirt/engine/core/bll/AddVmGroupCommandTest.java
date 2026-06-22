package org.ovirt.engine.core.bll;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.ovirt.engine.core.bll.context.CommandContext;
import org.ovirt.engine.core.common.action.VmGroupOperationParameters;
import org.ovirt.engine.core.common.businessentities.VmGroup;
import org.ovirt.engine.core.common.errors.EngineMessage;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.core.dao.VmGroupDao;

@MockitoSettings(strictness = Strictness.LENIENT)
public class AddVmGroupCommandTest extends BaseCommandTest {

    private static final String VM_GROUP_NAME = "testGroup";

    @Mock
    private VmGroupDao vmGroupDao;

    private VmGroup vmGroup;
    private VmGroupOperationParameters parameters;

    @InjectMocks
    private AddVmGroupCommand<VmGroupOperationParameters> cmd =
            new AddVmGroupCommand<>(createParameters(), CommandContext.createContext(""));

    @BeforeEach
    public void setUp() {
        // vmGroupDao.get() вызывается в getVmGroup() — возвращаем нашу группу для любого id
        when(vmGroupDao.get(any())).thenReturn(vmGroup);
    }

    private VmGroupOperationParameters createParameters() {
        vmGroup = new VmGroup();
        vmGroup.setId(Guid.newGuid());
        vmGroup.setName(VM_GROUP_NAME);
        vmGroup.setParentGroupId(null);

        parameters = new VmGroupOperationParameters(vmGroup);
        return parameters;
    }

    @Test
    public void testValidateSuccess() {
        // Имя свободно — getByName возвращает null
        when(vmGroupDao.getByName(VM_GROUP_NAME)).thenReturn(null);
        // Родительской группы нет (parentGroupId == null) — parentGroupExists() сразу возвращает VALID

        ValidateTestUtils.runAndAssertValidateSuccess(cmd);
    }

    @Test
    public void testValidateNameAlreadyUsed() {
        // Имя занято — в БД уже есть группа с тем же именем, но другим id
        VmGroup existingGroup = new VmGroup();
        existingGroup.setId(Guid.newGuid()); // другой id — значит это не та же самая группа
        existingGroup.setName(VM_GROUP_NAME);
        when(vmGroupDao.getByName(VM_GROUP_NAME)).thenReturn(existingGroup);

        ValidateTestUtils.runAndAssertValidateFailure(cmd, EngineMessage.VM_GROUP_CANNOT_DO_ACTION_NAME_IN_USE);
    }

    @Test
    public void testValidateParentGroupNotExists() {
        // Имя свободно
        when(vmGroupDao.getByName(VM_GROUP_NAME)).thenReturn(null);

        // Выставляем несуществующий parentGroupId
        Guid nonExistentParentId = Guid.newGuid();
        vmGroup.setParentGroupId(nonExistentParentId);

        // get() для parentGroupId вернёт null — родительская группа не найдена
        when(vmGroupDao.get(nonExistentParentId)).thenReturn(null);

        ValidateTestUtils.runAndAssertValidateFailure(cmd, EngineMessage.VALIDATION_VM_GROUP_PARENT_GROUP_NOT_EXISTS);
    }
}
