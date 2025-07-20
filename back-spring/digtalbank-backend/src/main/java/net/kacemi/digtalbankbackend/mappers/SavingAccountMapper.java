package net.kacemi.digtalbankbackend.mappers;

import net.kacemi.digtalbankbackend.dtos.SavingAccountDTO;
import net.kacemi.digtalbankbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SavingAccountMapper {
    private CustomerMapper customerMapper;

    @Autowired
    public SavingAccountMapper(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    public SavingAccountDTO fromSavingAccount(SavingAccount savingAccount) {
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingAccountDTO);
        if(savingAccount.getCustomer() != null) {
            savingAccountDTO.setCustomer(customerMapper.fromCustomer(savingAccount.getCustomer()));
        }
        savingAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingAccountDTO;
    }

    public SavingAccount fromSavingAccountDTO(SavingAccountDTO savingAccountDTO) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingAccountDTO,savingAccount);
        return savingAccount;
    }
}
