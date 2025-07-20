package net.kacemi.digtalbankbackend.mappers;

import net.kacemi.digtalbankbackend.dtos.CurrentAccountDTO;
import net.kacemi.digtalbankbackend.entities.CurrentAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CurrentAccountMapper {
    private CustomerMapper customerMapper;

    @Autowired
    public CurrentAccountMapper(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    public CurrentAccountDTO fromCurrentAccount(CurrentAccount currentAccount) {
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        BeanUtils.copyProperties(currentAccount,currentAccountDTO);
        if(currentAccount.getCustomer() != null) {
            currentAccountDTO.setCustomer( customerMapper.fromCustomer(currentAccount.getCustomer()));
        }
        currentAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentAccountDTO;
    }

    public CurrentAccount fromCurrentAccountDTO(CurrentAccountDTO currentAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDTO,currentAccount);
        return currentAccount;
    }
}
