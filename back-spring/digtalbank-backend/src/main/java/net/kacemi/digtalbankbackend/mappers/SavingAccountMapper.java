package net.kacemi.digtalbankbackend.mappers;

import net.kacemi.digtalbankbackend.dtos.SavingAccountDTO;
import net.kacemi.digtalbankbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service
public class SavingAccountMapper {

    public SavingAccountDTO fromSavingAccount(SavingAccount savingAccount) {
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingAccountDTO);
        return savingAccountDTO;
    }

    public SavingAccount fromSavingAccountDTO(SavingAccountDTO savingAccountDTO) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingAccountDTO,savingAccount);
        return savingAccount;
    }
}
