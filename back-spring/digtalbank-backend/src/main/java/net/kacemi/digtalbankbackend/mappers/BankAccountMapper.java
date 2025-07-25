package net.kacemi.digtalbankbackend.mappers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kacemi.digtalbankbackend.dtos.BankAccountDTO;
import net.kacemi.digtalbankbackend.dtos.CurrentAccountDTO;
import net.kacemi.digtalbankbackend.dtos.SavingAccountDTO;
import net.kacemi.digtalbankbackend.entities.BankAccount;
import net.kacemi.digtalbankbackend.entities.CurrentAccount;
import net.kacemi.digtalbankbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class BankAccountMapper {

    private final CurrentAccountMapper currentAccountMapper;
    private final SavingAccountMapper savingAccountMapper;



    public BankAccountDTO fromBankAccount(BankAccount bankAccount) {

        if( bankAccount instanceof CurrentAccount) {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
//            log.info("info customer "+ bankAccount.getCustomer());
            currentAccount.setCustomer(bankAccount.getCustomer());
            return currentAccountMapper.fromCurrentAccount(currentAccount);
        }else{
            SavingAccount savingAccount = (SavingAccount) bankAccount;
//            log.info("info customer "+ bankAccount.getCustomer());

            savingAccount.setCustomer(bankAccount.getCustomer());
            return  savingAccountMapper.fromSavingAccount(savingAccount);
        }

    }

    public BankAccount fromBankAccountDTO(BankAccountDTO bankAccountDTO) {


        if( bankAccountDTO instanceof CurrentAccountDTO) {
            CurrentAccountDTO currentAccountDTO = (CurrentAccountDTO) bankAccountDTO;
            return currentAccountMapper.fromCurrentAccountDTO(currentAccountDTO);
        }else{
            SavingAccountDTO savingAccountDTO = (SavingAccountDTO) bankAccountDTO;
            return  savingAccountMapper.fromSavingAccountDTO(savingAccountDTO);
        }
    }
}
