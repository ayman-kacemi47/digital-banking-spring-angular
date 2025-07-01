package net.kacemi.digtalbankbackend.mappers;

import net.kacemi.digtalbankbackend.dtos.CurrentAccountDTO;
import net.kacemi.digtalbankbackend.entities.CurrentAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service
public class CurrentAccountMapper {
    public CurrentAccountDTO fromCurrentAccount(CurrentAccount currentAccount) {
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        BeanUtils.copyProperties(currentAccount,currentAccountDTO);
        return currentAccountDTO;
    }

    public CurrentAccount fromCurrentAccountDTO(CurrentAccountDTO currentAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDTO,currentAccount);
        return currentAccount;
    }
}
